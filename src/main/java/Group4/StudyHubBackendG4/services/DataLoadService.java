package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.*;
import Group4.StudyHubBackendG4.utils.RoleUtil;
import Group4.StudyHubBackendG4.utils.converters.AsignaturaConverter;
import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
import com.github.javafaker.Faker;
import jakarta.mail.MessagingException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataLoadService {

    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private DocenteRepo docenteRepo;
    @Autowired
    private CarreraRepo carreraRepo;
    @Autowired
    private AsignaturaRepo asignaturaRepo;
    @Autowired
    private PeriodoExamenRepo periodoExamenRepo;
    @Autowired
    private HorarioAsignaturaRepo horarioAsignaturaRepo;
    @Autowired
    private HorarioDiasRepo horarioDiasRepo;
    @Autowired
    private DocenteHorarioAsignaturaRepo docenteHorarioAsignaturaRepo;
    @Autowired
    private AsignaturaService asignaturaService;
    @Autowired
    private CarreraService carreraService;
    @Autowired
    private AsignaturaConverter asignaturaConverter;
    @Autowired
    private DocenteAsignaturaRepo docenteAsignaturaRepo;
    @Autowired
    private PreviaturasRepo previaturasRepo;

    @Autowired
    private CarreraCoordinadorRepo carreraCoordinadorRepo;
    @Autowired
    private ActividadService actividadService;

    private Faker faker = new Faker();

    public void registerUsers() throws IOException, MessagingException {
        // Cargar administradores
        for (int i = 0; i < 2; i++) {
            register(generateRandomUser("A", "1"));
        }

        // Cargar usuarios funcionario
        for (int i = 0; i < 4; i++) {
            register(generateRandomUser("F", "2"));
        }

        // Cargar usuarios coordinador
        for (int i = 0; i < 4; i++) {
            register(generateRandomUser("C", "3"));
        }

        // Cargar usuarios estudiante
        for (int i = 0; i < 50; i++) {
            register(generateRandomUser("E", "4"));
        }
    }

    public void registerDocentes() {
        for (int i = 0; i < 50; i++) {
            DtNuevoDocente dtNuevoDocente = generateRandomDocente();
            nuevoDocente(dtNuevoDocente);
        }
    }

    public void registerCarreras() throws IOException {
        List<Usuario> coordinadores = usuarioRepo.findByRol("C");
        Set<String> nombresCarreras = new HashSet<>();

        // Generar 15 nombres de carreras únicos
        while (nombresCarreras.size() < 15) {
            String nombre = faker.educator().course();
            if (!nombresCarreras.contains(nombre)) {
                nombresCarreras.add(nombre);
            }
        }

        // Crear las carreras
        for (String nombre : nombresCarreras) {
            DtNuevaCarrera dtNuevaCarrera = generateRandomCarrera(coordinadores, nombre);
            nuevaCarrera(dtNuevaCarrera);
        }
    }

    public void registerAsignaturas() throws IOException {
        List<Carrera> carreras = carreraRepo.findAll();
        List<Docente> docentes = docenteRepo.findAll();

        for (Carrera carrera : carreras) {
            Set<String> nombresAsignaturas = new HashSet<>();
            for (int i = 0; i < 20; i++) {
                String nombre = "Asignatura " + carrera.getIdCarrera() + "." + i;
                DtNuevaAsignatura dtNuevaAsignatura = generateRandomAsignatura(carrera, docentes, nombre);
                altaAsignatura(dtNuevaAsignatura);
            }
        }
    }

    public void registerPeriodos() {
        List<Carrera> carreras = carreraRepo.findAll();

        for (Carrera carrera : carreras) {
            // Generar periodos de examen
            List<DtPeriodoExamenRequest> periodos = generatePeriodosExamen();
            for (DtPeriodoExamenRequest periodo : periodos) {
                altaPeriodoDeExamen(carrera.getIdCarrera(), periodo);
            }
        }
    }

    public void registerHorarios() {
        List<Asignatura> asignaturas = asignaturaRepo.findAll();

        for (Asignatura asignatura : asignaturas) {
            List<Docente> docentes = docenteAsignaturaRepo.findDocentesByAsignaturaId(asignatura.getIdAsignatura());
            List<DtNuevoHorarioAsignatura> horarios = generateHorarios(docentes);

            for (DtNuevoHorarioAsignatura horario : horarios) {
                registroHorarios(asignatura.getIdAsignatura(), horario);
            }
        }
    }


    /* -----------------------------------------------------------------------------------------------*/
    /* -----------------------------------------------------------------------------------------------*/
    /* -----------------------------------------------------------------------------------------------*/

    private List<DtNuevoHorarioAsignatura> generateHorarios(List<Docente> docentes) {
        List<DtNuevoHorarioAsignatura> horarios = new ArrayList<>();
        int[] anios = {2023, 2024};

        for (int anio : anios) {
            for (int i = 0; i < 3; i++) {
                int idDocente = docentes.get(ThreadLocalRandom.current().nextInt(docentes.size())).getIdDocente();
                List<DtHorarioDias> dtHorarioDiasList = generateRandomHorarioDias();

                DtNuevoHorarioAsignatura horario = new DtNuevoHorarioAsignatura(idDocente, anio, dtHorarioDiasList);
                horarios.add(horario);
            }
        }

        return horarios;
    }

    private List<DtHorarioDias> generateRandomHorarioDias() {
        List<DtHorarioDias> dtHorarioDiasList = new ArrayList<>();
        DiaSemana[] dias = DiaSemana.values();

        int numDias = ThreadLocalRandom.current().nextInt(1, 3); // 1 o 2 HorarioDias
        List<DiaSemana> diasSeleccionados = new ArrayList<>();

        for (int i = 0; i < numDias; i++) {
            DiaSemana diaSemana;
            do {
                diaSemana = dias[ThreadLocalRandom.current().nextInt(dias.length)];
            } while (diasSeleccionados.contains(diaSemana));

            diasSeleccionados.add(diaSemana);

            String horaInicio = generateRandomHora();
            String horaFin = generateRandomHoraPosterior(horaInicio);

            DtHorarioDias dtHorarioDias = new DtHorarioDias();
            dtHorarioDias.setDiaSemana(diaSemana);
            dtHorarioDias.setHoraInicio(horaInicio);
            dtHorarioDias.setHoraFin(horaFin);

            dtHorarioDiasList.add(dtHorarioDias);
        }

        return dtHorarioDiasList;
    }

    private String generateRandomHora() {
        int horas = ThreadLocalRandom.current().nextInt(8, 23); // Horario entre 8:00 y 22:59
        int minutos = ThreadLocalRandom.current().nextInt(0, 2) * 30; // Minutos pueden ser 0 o 30
        return String.format("%02d:%02d", horas, minutos);
    }

    private String generateRandomHoraPosterior(String horaInicio) {
        int minutosInicio = convertirHora(horaInicio);
        int[] posiblesDuraciones = {30, 60, 90, 120};
        int duracion = posiblesDuraciones[ThreadLocalRandom.current().nextInt(posiblesDuraciones.length)];
        int minutosFin = minutosInicio + duracion;
        int horas = minutosFin / 60;
        int minutos = minutosFin % 60;
        return String.format("%02d:%02d", horas, minutos);
    }
    private List<DtPeriodoExamenRequest> generatePeriodosExamen() {
        List<DtPeriodoExamenRequest> periodos = new ArrayList<>();
        String[] nombresPeriodos = {
                "Febrero 2023", "Julio 2023", "Diciembre 2023",
                "Febrero 2024", "Julio 2024", "Diciembre 2024"
        };
        int[] anios = {2023, 2023, 2023, 2024, 2024, 2024};
        Month[] meses = {Month.FEBRUARY, Month.JULY, Month.DECEMBER, Month.FEBRUARY, Month.JULY, Month.DECEMBER};

        for (int i = 0; i < nombresPeriodos.length; i++) {
            DtFecha fechaInicio = generateRandomFecha(anios[i], meses[i]);
            DtFecha fechaFin = new DtFecha(fechaInicio.getAnio(), fechaInicio.getMes(), fechaInicio.getDia() + 15);
            periodos.add(new DtPeriodoExamenRequest(nombresPeriodos[i], fechaInicio, fechaFin));
        }

        return periodos;
    }

    private DtFecha generateRandomFecha(int anio, Month mes) {
        int diaInicio = ThreadLocalRandom.current().nextInt(1, mes.length(LocalDate.of(anio, mes, 1).isLeapYear()) - 14);
        return new DtFecha(anio, mes.getValue(), diaInicio);
    }

    private DtNuevaAsignatura generateRandomAsignatura(Carrera carrera, List<Docente> docentes, String nombre) {
        int creditos = ThreadLocalRandom.current().nextInt(1, 10);
        String descripcion = faker.lorem().sentence();
        String departamento = faker.educator().university();
        boolean tieneExamen = faker.bool().bool();
        boolean activa = true;
        List<Integer> idDocentes = new ArrayList<>();

        // Asignar docentes aleatoriamente
        for (int j = 0; j < ThreadLocalRandom.current().nextInt(1, 4); j++) {
            Docente docente = docentes.get(ThreadLocalRandom.current().nextInt(docentes.size()));
            idDocentes.add(docente.getIdDocente());
        }
        // Obtener posibles previaturas
        List<Asignatura> posiblesPrevias = asignaturaRepo.findByCarrera(carrera);
        List<Integer> previas = new ArrayList<>();
        if (posiblesPrevias != null && !posiblesPrevias.isEmpty()) {
            Collections.shuffle(posiblesPrevias);
            int numPrevias = Math.min(ThreadLocalRandom.current().nextInt(0, 4), posiblesPrevias.size());
            for (int k = 0; k < numPrevias; k++) {
                previas.add(posiblesPrevias.get(k).getIdAsignatura());
            }
        }

        return new DtNuevaAsignatura(carrera.getIdCarrera(), idDocentes, nombre, creditos, descripcion, departamento, tieneExamen, activa, previas);
    }

    private DtNuevaCarrera generateRandomCarrera(List<Usuario> coordinadores, String nombre) {
        String descripcion = faker.lorem().sentence();
        String requisitos = faker.lorem().paragraph();
        int duracion = ThreadLocalRandom.current().nextInt(1, 7); // Duración entre 1 y 6 años
        int idCoordinador = coordinadores.get(ThreadLocalRandom.current().nextInt(coordinadores.size())).getIdUsuario();

        return new DtNuevaCarrera(nombre, descripcion, requisitos, duracion, idCoordinador);
    }
    private DtNuevoDocente generateRandomDocente() {
        String nombre = faker.name().firstName();
        String apellido = faker.name().lastName();
        String nombreCompleto = nombre + " " + apellido;
        int codigoDocente = generateRandomCodigoDocente();

        return new DtNuevoDocente(codigoDocente, nombreCompleto);
    }
    private int generateRandomCodigoDocente() {
        return faker.number().numberBetween(1000, 9999); // Genera un número de 4 dígitos
    }
    private DtNuevoUsuario generateRandomUser(String rol, String prefix) {
        String nombre = faker.name().firstName();
        String apellido = faker.name().lastName();
        String email = generateEmail(nombre, apellido);
        String fechaNacimiento = generateRandomDateOfBirth();
        String cedula = generateRandomCedulaWithPrefix(prefix);
        return new DtNuevoUsuario(nombre, apellido, email, fechaNacimiento, cedula, "123", rol);
    }

    private String generateEmail(String nombre, String apellido) {
        return String.format("%s.%s@example.com", nombre.toLowerCase(), apellido.toLowerCase());
    }

    private String generateRandomDateOfBirth() {
        LocalDate startDate = LocalDate.of(1950, 1, 1);
        LocalDate endDate = LocalDate.of(2002, 1, 1);
        long start = startDate.toEpochDay();
        long end = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(start, end);
        LocalDate randomDate = LocalDate.ofEpochDay(randomEpochDay);
        return randomDate.toString();
    }

    private String generateRandomCedulaWithPrefix(String prefix) {
        int randomNumber = ThreadLocalRandom.current().nextInt(1000000, 10000000);
        return prefix + String.format("%07d", randomNumber);
    }

    private void register(DtNuevoUsuario dtNuevoUsuario) throws IOException, MessagingException {
        Optional<Usuario> existingUsuario = Optional.ofNullable(usuarioRepo.findByCedula(dtNuevoUsuario.getCedula()));
        Usuario usuario = existingUsuario.orElseGet(Usuario::new)
                .DataLoadUserFromDtNewUser(dtNuevoUsuario);
        usuarioRepo.save(usuario);
        Actividad actividad = new Actividad();
        actividad.setUsuario(usuario);
        actividad.setFechaHora(LocalDateTime.now());
        actividad.setAccion("Registro de Usuario");
        actividadService.save(actividad);
    }

    private void nuevoDocente(DtNuevoDocente dtNuevoDocente) {
        Optional<Docente> existingDocente = Optional.ofNullable(docenteRepo.findByCodigoDocente(dtNuevoDocente.getCodigoDocente()));
        Docente docente = existingDocente.orElseGet(Docente::new)
                .DocenteFromDtNuevoDocente(dtNuevoDocente);
        docenteRepo.save(docente);
    }
    private void nuevaCarrera(DtNuevaCarrera dtNuevaCarrera) {

        Optional<Carrera> existingCarrera = Optional.ofNullable(carreraRepo.findByNombre(dtNuevaCarrera.getNombre()));

        if (existingCarrera.isPresent()) {
            System.out.println("La carrera con el nombre " + dtNuevaCarrera.getNombre() + " ya existe.");
            return;
        }

        Carrera carrera = new Carrera();
        carrera.setNombre(dtNuevaCarrera.getNombre());
        carrera.setDescripcion(dtNuevaCarrera.getDescripcion());
        carrera.setRequisitos(dtNuevaCarrera.getRequisitos());
        carrera.setDuracion(dtNuevaCarrera.getDuracion());

        carreraRepo.save(carrera);
        asignarCoordinador(carrera.getIdCarrera(), dtNuevaCarrera.getIdCoordinador());
    }

    private void asignarCoordinador(Integer idCarrera, Integer idCoordinador) {
        Optional<Carrera> carreraOpt = carreraRepo.findById(idCarrera);
        Optional<Usuario> coordinadorOpt = usuarioRepo.findById(idCoordinador);

        if (carreraOpt.isPresent() && coordinadorOpt.isPresent()) {
            Carrera carrera = carreraOpt.get();
            Usuario coordinador = coordinadorOpt.get();
            if (carreraCoordinadorRepo.existsByCarreraAndUsuario(carrera, coordinador)) {
                return;
            }
            if (!RoleUtil.getRoleName(coordinador.getRol()).equals("Coordinador")) {
                return;
            }
            CarreraCoordinador carreraCoordinador = new CarreraCoordinador();
            carreraCoordinador.setCarrera(carrera);
            carreraCoordinador.setUsuario(coordinador);
            carreraCoordinadorRepo.save(carreraCoordinador);
        }
    }

    private void altaAsignatura(DtNuevaAsignatura dtNuevaAsignatura) {
        Carrera carrera = carreraRepo.findById(dtNuevaAsignatura.getIdCarrera()).orElse(null);
        if (carrera == null) {
            System.out.println("No se encontró la carrera");
            return;
        }

        List<Integer> idDocentes = dtNuevaAsignatura.getIdDocentes();
        if (idDocentes == null || idDocentes.isEmpty()) {
            System.out.println("No se cargó ningun docente");
            return;
        }

        List<Docente> docentes = idDocentes.stream()
                .map(docenteRepo::findById)
                .map(optionalDocente -> optionalDocente.orElse(null))
                .toList();

        if (docentes.contains(null)) {
            System.out.println("No se encontró algun docente");
            return;
        }

        if (asignaturaRepo.existsByNombreAndCarrera(dtNuevaAsignatura.getNombre(), carrera)) {
            System.out.println("Ya existe la asignatura para la carrera");
            return;
        }

        if (asignaturaService.validarCircularidad(null, dtNuevaAsignatura.getPreviaturas())) {
            System.out.println("No se cumple circularidad");
            return;
        }

        DtAsignatura dtAsignatura = dtNuevaAsignatura.dtAsignaturaFromDtNuevaAsignatura(dtNuevaAsignatura);
        Asignatura asignatura = asignaturaConverter.convertToEntity(dtAsignatura);

        // Prepare and validate Previaturas
        List<Integer> idsPreviaturas = dtNuevaAsignatura.getPreviaturas();
        List<Previaturas> previaturas = new ArrayList<>();
        if (idsPreviaturas != null && !idsPreviaturas.isEmpty()) {
            for (Integer idPrevia : idsPreviaturas) {
                Asignatura previaAsignatura = asignaturaRepo.findById(idPrevia).orElse(null);
                if (previaAsignatura == null) {
                    System.out.println("No se encontró previatura");
                    return;
                }

                // Validar si pertenecen a la misma carrera
                if (!previaAsignatura.getCarrera().getIdCarrera().equals(carrera.getIdCarrera())) {
                    System.out.println("Previatura no pertenece a la misma carrera");
                    return;
                }

                Previaturas previatura = new Previaturas();
                previatura.setAsignatura(asignatura);
                previatura.setPrevia(previaAsignatura);
                previaturas.add(previatura);
            }
        }

        // Save Asignatura
        asignaturaRepo.save(asignatura);

        // Save DocenteAsignatura
        for (Docente docente : docentes) {
            DocenteAsignatura da = new DocenteAsignatura();
            da.setDocente(docente);
            da.setAsignatura(asignatura);
            docenteAsignaturaRepo.save(da);
        }

        // Save Previaturas
        for (Previaturas previatura : previaturas) {
            previaturasRepo.save(previatura);
        }
    }

    public void altaPeriodoDeExamen(Integer idCarrera, DtPeriodoExamenRequest fechas) {
        try {
            LocalDate fechaInicio = fechas.getInicio().convertToLocalDate();
            LocalDate fechaFin = fechas.getFin().convertToLocalDate();

            Carrera carrera = carreraRepo.findById(idCarrera).orElse(null);
            if(carrera == null) {
                System.out.println("La carrera no existe");
                return;
            }

            String message = validatePeriodo(carrera, fechaInicio, fechaFin);
            if (!message.isEmpty()) {
                System.out.println(message);
                return;
            }

            // Crear y guardar el nuevo período
            PeriodoExamen nuevoPeriodo = new PeriodoExamen();
            nuevoPeriodo.setCarrera(carrera);
            nuevoPeriodo.setFechaInicio(fechaInicio);
            nuevoPeriodo.setFechaFin(fechaFin);
            nuevoPeriodo.setNombre(fechas.getNombre());

            periodoExamenRepo.save(nuevoPeriodo);
        } catch (DateTimeException e) {
            System.out.println("Se ha ingresado una fecha invalida");
        }
    }

    private String validatePeriodo(Carrera carrera, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaFin.isBefore(fechaInicio)) {
            return "La fecha de fin no puede ser antes de la fecha de inicio.";
        }
        return periodoExamenRepo.findByCarreraAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
                carrera, fechaFin, fechaInicio).isEmpty() ? "" : "El período ingresado se solapa con un período existente.";
    }

    public void registroHorarios(Integer idAsignatura, DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura) {
        Asignatura asignatura = asignaturaRepo.findById(idAsignatura)
                .orElse(null);

        if (asignatura == null) {
            System.out.println("idAsignatura invalido");
            return;
        }

        Docente docente = docenteRepo.findById(dtNuevoHorarioAsignatura.getIdDocente())
                .orElse(null);

        if (docente == null) {
            System.out.println("idDocente invalido");
            return;
        }

        List<HorarioDias> existingHorarioDias = docenteHorarioAsignaturaRepo.findHorarioDiasByDocenteIdAndAnio(docente.getIdDocente(), dtNuevoHorarioAsignatura.getAnio());

        for (DtHorarioDias newHorarioDia : dtNuevoHorarioAsignatura.getDtHorarioDias()) {
            DiaSemana diaSemana = newHorarioDia.getDiaSemana();
            String horaInicioStr = newHorarioDia.getHoraInicio();
            String horaFinStr = newHorarioDia.getHoraFin();

            // Validar y convertir horaInicio y horaFin
            if (!esHoraValida(horaInicioStr) || !esHoraValida(horaFinStr)) {
                System.out.println("Formato de hora inválido. Use HH:mm");
                return;
            }

            int horaInicio = convertirHora(horaInicioStr);
            int horaFin = convertirHora(horaFinStr);

            if (horaInicio >= horaFin) {
                System.out.println("horaInicio debe ser menor a horaFin, y los valores deben ser válidos (por ejemplo, 10:30 para 10:30)");
                return;
            }

            boolean solapado = existingHorarioDias.stream()
                    .filter(horario -> horario.getDiaSemana().equals(diaSemana))
                    .anyMatch(existingHorarioDia -> {
                        int existingHoraInicio = convertirHora(existingHorarioDia.getHoraInicio());
                        int existingHoraFin = convertirHora(existingHorarioDia.getHoraFin());
                        return horaInicio < existingHoraFin && horaFin > existingHoraInicio;
                    });

            if (solapado) {
                System.out.println("Horarios superpuestos detectados para el dia " + diaSemana);
                return;
            }

        }
        HorarioAsignatura horarioAsignatura = createAndSaveHorarioAsignatura(asignatura, dtNuevoHorarioAsignatura.getAnio());
        createAndSaveHorarioDias(horarioAsignatura, dtNuevoHorarioAsignatura.getDtHorarioDias());
        createAndSaveDocenteHorarioAsignatura(docente, horarioAsignatura);
    }

    private boolean esHoraValida(String horaStr) {
        String[] partes = horaStr.split(":");
        if (partes.length != 2) {
            return false;
        }

        try {
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);

            return horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int convertirHora(String horaStr) {
        String[] partes = horaStr.split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        return horas * 60 + minutos;
    }

    private HorarioAsignatura createAndSaveHorarioAsignatura(Asignatura asignatura, Integer anio) {
        HorarioAsignatura horarioAsignatura = new HorarioAsignatura();
        horarioAsignatura.setAsignatura(asignatura);
        horarioAsignatura.setAnio(anio);
        return horarioAsignaturaRepo.save(horarioAsignatura);
    }

    private void createAndSaveHorarioDias(HorarioAsignatura horarioAsignatura, List<DtHorarioDias> dtHorarioDiasList) {
        for (DtHorarioDias dtHorarioDias : dtHorarioDiasList) {
            HorarioDias horarioDias = new HorarioDias();
            horarioDias.setDiaSemana(dtHorarioDias.getDiaSemana());
            horarioDias.setHoraInicio(dtHorarioDias.getHoraInicio());
            horarioDias.setHoraFin(dtHorarioDias.getHoraFin());
            horarioDias.setHorarioAsignatura(horarioAsignatura);
            horarioDiasRepo.save(horarioDias);
        }
    }

    private void createAndSaveDocenteHorarioAsignatura(Docente docente, HorarioAsignatura horarioAsignatura) {
        DocenteHorarioAsignatura docenteHorarioAsignatura = new DocenteHorarioAsignatura();
        docenteHorarioAsignatura.setDocente(docente);
        docenteHorarioAsignatura.setHorarioAsignatura(horarioAsignatura);
        docenteHorarioAsignaturaRepo.save(docenteHorarioAsignatura);
    }
}
