import sys
from typing import Any, Optional
import warnings

from google.api_core.extended_operation import ExtendedOperation
from google.cloud import compute_v1

STOPPED_MACHINE_STATUS = (
	compute_v1.Instance.Status.TERMINATED.name,
	compute_v1.Instance.Status.STOPPED.name,
)

def wait_for_extended_operation(operation: ExtendedOperation, verbose_name: str="operation", timeout: int = 300) -> Any:
	''' This method will wait for the extended (long-running) operation to complete
	If the operation is successful, it will return its result '''
	result = operation.result(timeout=timeout)
	if operation.error_code:
		print("Error durint {verbose_name} : [Code: {ec}: {err_msg}".format(verbose_name=verbose_name, ec=operation.error_code, err_msg=operation.error_message))
		print("Operation ID : {op_name}".format(op_name=operation.name), file=sys.stderr, flush=True)
		raise operation.exception() or RuntimeError(operation.error_message)
	if operation.warnings:
		print("Warnings during {verbose_name}:\n".format(verbose_name=verbose_name), file=sys.stderr, flush=True)
		for warning in operation.warnings:
			print(" - {war_code}: {war_msg}".format(war_code=warning.code, war_msg=warning.message), file=sys.stderr, flush=True)

	return result

def create_image_from_disk(project_id: str, zone: str, source_disk_name: str, image_name: str, storage_location: Optional[str]=None, force_create: bool = False) -> compute_v1.Image:
	''' Creates a new disk image '''
	image_client = compute_v1.ImagesClient()
	disk_client = compute_v1.DisksClient()
	instance_client = compute_v1.InstanceClient()

	# Get source disk
	disk = disk_client.get(project=project_id, zone=zone, disk=source_disk_name)

	for disk_user in disk.users:
		instance = instance_client.get(project=project_id, zone=zone, instance=disk_user)
		if instance.status in STOPPED_MACHINE_STATUS:
			continue
		if not force_create:
			raise RuntimeError(f"Instance {disk_user} should be stopped. For Windows instances please "
                f"stop the instance using `GCESysprep` command. For Linux instances just "
                f"shut it down normally. You can supress this error and create an image of"
                f"the disk by setting `force_create` parameter to true (not recommended). \n"
                f"More information here: \n"
                f" * https://cloud.google.com/compute/docs/instances/windows/creating-windows-os-image#api \n"
                f" * https://cloud.google.com/compute/docs/images/create-delete-deprecate-private-images#prepare_instance_for_image")
		else:
			warnings.warn(
				f"Warning: The `force_create` option may compromise the integrity of your image. "
                f"Stop the {disk_user} instance before you create the image if possible."
			)

	# Create image
	image = compute_v1.Image()
	image.source_disk = disk.self_link
	image.name = image_name
	if storage_location:
		image.storage_location = [storage_location]

	operation = image_client.insert(project=project_id, image_resource=image)
	wait_for_extended_operation(operation, "image creating from disk")
	return image_client.get(project=project_id, image=image_name, )
