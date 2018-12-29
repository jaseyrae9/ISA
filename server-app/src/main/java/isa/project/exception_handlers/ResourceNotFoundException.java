package isa.project.exception_handlers;

public class ResourceNotFoundException extends Exception {
	private static final long serialVersionUID = 5198244129753372024L;
	private String resourceId;

	public ResourceNotFoundException(String resourceId, String message) {
		super(message);
		this.resourceId = resourceId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getMessage() {
		return resourceId + ": " + super.getMessage();
	}
}
