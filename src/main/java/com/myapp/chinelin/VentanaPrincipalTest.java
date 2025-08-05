import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;

public class VentanaPrincipalTest {
    @Test
    public void testTituloVentana() {
        VentanaPrincipal vp = new VentanaPrincipal();
        assertEquals("Sistema Chinelín", vp.getTitle());
    }
}
