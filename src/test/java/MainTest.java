import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MainTest {

  @Test
  void getHelloWorld() {
    assertEquals(Main.getHelloWorld(), "Hello world!", "Incorrect capitalisation");
  }
}
