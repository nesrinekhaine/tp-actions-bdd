import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class StudentsTest {

    private Connection conn;

    @BeforeEach
    void setUp() throws Exception {
        conn = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/tp",
            "postgres",
            "postgres"
        );

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                "TRUNCATE TABLE students RESTART IDENTITY CASCADE"
            );
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    @Test
    void testAddAndGet() throws Exception {
        int id = Students.addStudent(conn, "Amel", 15);

        var student = Students.getStudent(conn, id);

        assertTrue(student.isPresent());
        assertEquals("Amel", student.get().name);
        assertEquals(15.0, student.get().grade, 0.01);
    }

    @Test
    void testGetMissing() throws Exception {
        var student = Students.getStudent(conn, 999);

        assertTrue(student.isEmpty());
    }

    @Test
    void testAverage() throws Exception {
        Students.addStudent(conn, "Amel", 10);
        Students.addStudent(conn, "Karim", 14);

        assertEquals(12.0, Students.averageGrade(conn), 0.01);
    }
}
