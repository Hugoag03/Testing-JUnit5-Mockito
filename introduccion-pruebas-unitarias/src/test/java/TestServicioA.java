import com.ejemplos.pruebas.ServicioA;
import com.ejemplos.pruebas.ServicioAImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestServicioA {

    @Test
    void testSumar(){
        int a = 2, b = 2;
        ServicioA servicioA = new ServicioAImpl();

        //valor real - valor esperado
        Assertions.assertEquals(servicioA.sumar(a,b), 5);
    }
}
