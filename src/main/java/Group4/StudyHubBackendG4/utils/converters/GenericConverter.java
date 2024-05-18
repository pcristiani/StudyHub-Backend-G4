package Group4.StudyHubBackendG4.utils.converters;

public interface GenericConverter<E, D> {
    D convertToDto(E entity);
    E convertToEntity(D dto);
}
