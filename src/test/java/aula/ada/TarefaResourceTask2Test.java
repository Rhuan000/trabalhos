package aula.ada;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class TarefaResourceTask2Test {

    @Test
    void retornaConflitoAoConcluirTarefaCancelada() {
        long id = given()
                .contentType("application/json")
                .body("""
                        {"titulo":"Tarefa cancelada","status":"CANCELADA"}
                        """)
                .when()
                .post("/tarefas")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
                .contentType("application/json")
                .body("""
                        {"titulo":"Tarefa concluida","status":"CONCLUIDA"}
                        """)
                .when()
                .put("/tarefas/{id}", id)
                .then()
                .statusCode(409)
                .body("codigo", is("TRANSICAO_TAREFA_INVALIDA"));
    }

    @Test
    void rejeitaTituloInvalido() {
        given()
                .contentType("application/json")
                .body("""
                        {"titulo":"x"}
                        """)
                .when()
                .post("/tarefas")
                .then()
                .statusCode(400);
    }
}
