package Group4.StudyHubBackendG4.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class SimpleCoursesController {

    @GetMapping("/course-relations")
    public String getCourseRelations() {
        return "graph G {"
                + "node [shape=rectangle, style=filled, color=lightblue];"
                + "rankdir=TB;"

                + "subgraph cluster_semester1 {"
                + "label=\"Semestre 1\";"
                + "Arquitectura;"
                + "Discreta1 [label=\"Discreta y Lógica 1\"];"
                + "Principios [label=\"Principios de Programación\"];"
                + "Matematica;"
                + "Ingles1 [label=\"Inglés Técnico\"];"
                + "Invisible1S1 [label=\"\", style=invis, height=0];"
                + "}"

                + "subgraph cluster_semester2 {"
                + "label=\"Semestre 2\";"
                + "SO [label=\"Sistemas Operativos\"];"
                + "Discreta2 [label=\"Discreta y Lógica 2\"];"
                + "Estructuras [label=\"Estructuras de Datos y Algorit.\"];"
                + "BD1 [label=\"Bases de Datos 1\"];"
                + "Ingles2 [label=\"Inglés Técnico 2\"];"
                + "Invisible1S2 [label=\"\", style=invis, height=0];"
                + "}"

                + "subgraph cluster_semester3 {"
                + "label=\"Semestre 3\";"
                + "Redes [label=\"Redes de Computadoras\"];"
                + "Invisible1 [label=\"\", style=invis, height=0];"
                + "Programacion [label=\"Programación Avanzada\"];"
                + "BD2 [label=\"Bases de Datos 2\"];"
                + "Comunicacion [label=\"Comunicación Oral y Escrita\"];"
                + "Contabilidad;"
                + "}"

                + "subgraph cluster_semester4 {"
                + "label=\"Semestre 4\";"
                + "Admin [label=\"Administración de Infraestructura\"];"
                + "Probabilidad [label=\"Probabilidad y Estadística\"];"
                + "Software [label=\"Ingeniería de Software\"];"
                + "Aplicaciones [label=\"Programación de Aplicaciones\"];"
                + "Relaciones [label=\"Relaciones Pers. y Lab.\"];"
                + "Invisible1S4 [label=\"\", style=invis, height=0];"
                + "}"

                // Modified connections to maintain vertical structure and enforce ordering
                + "Arquitectura -- Discreta1 -- Principios -- Matematica -- Ingles1 -- Invisible1S1 [style=invis];"
                + "SO -- Discreta2 -- Estructuras -- BD1 -- Ingles2 -- Invisible1S2 [style=invis];"
                + "Redes -- Invisible1 -- Programacion -- BD2 -- Comunicacion -- Contabilidad -- Invisible1S4 [style=invis];"
                + "Invisible1S4 -- Admin [style=invis];"

                // Conexiones adicionales según las flechas de la segunda imagen
                + "Arquitectura -- SO [style=dotted, color=blue];"
                + "Redes -- Admin [style=dotted, color=blue];"
                + "Discreta1 -- Discreta2 [style=dotted, color=blue];"
                + "Discreta1 -- Estructuras [style=dotted, color=blue];"
                + "Discreta1 -- Probabilidad [color=red];"
                + "Discreta2 -- Probabilidad [color=red];"
                + "Principios -- Estructuras [style=dotted, color=blue];"
                + "Principios -- BD1 [style=dotted, color=blue];"
                + "Estructuras -- Programacion [style=dotted, color=blue];"
                + "Programacion -- Software [style=dotted, color=blue];"
                + "Programacion -- Aplicaciones [style=dotted, color=blue];"
                + "Estructuras -- Software [color=red];"
                + "Estructuras -- Aplicaciones [color=red];"
                + "Arquitectura -- Redes [style=dotted, color=blue];"
                + "SO -- Admin [color=red];"
                + "BD1 -- BD2 [style=dotted, color=blue];"
                + "BD1 -- Software [color=red];"
                + "BD1 -- Aplicaciones [color=red];"
                + "BD2 -- Aplicaciones [style=dotted, color=blue];"
                + "BD2 -- Software [style=dotted, color=blue];"
                //conexiones directas
                + "Arquitectura -- SO [color=black];"
                + "SO -- Redes [color=black];"
                + "Redes -- Admin [color=black];"
                + "Discreta1 -- Discreta2 [color=black];"
                + "Discreta2 -- Probabilidad [color=black];"
                + "Principios -- Estructuras [color=black];"
                + "Estructuras -- Programacion [color=black];"
                + "Programacion -- Software [color=black];"
                + "Matematica -- BD1 [color=black];"
                + "BD1 -- BD2 [color=black];"
                + "BD2 -- Aplicaciones [color=black];"
                + "Ingles1 -- Ingles2 [color=black];"
                + "Ingles2 -- Comunicacion [color=black];"
                + "Ingles2 -- Contabilidad [color=black];"
                + "Comunicacion -- Relaciones [color=black];"
                + "Contabilidad -- Relaciones [color=black];"
                + "}";
    }
}
