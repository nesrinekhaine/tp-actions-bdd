<?php

use PHPUnit\Framework\TestCase;

require_once __DIR__ . '/Students.php';

class StudentsTest extends TestCase
{
    private PDO $conn;

    protected function setUp(): void
    {
        $this->conn = new PDO(
            "pgsql:host=localhost;port=5432;dbname=tp",
            "postgres",
            "postgres"
        );

        $this->conn->setAttribute(
            PDO::ATTR_ERRMODE,
            PDO::ERRMODE_EXCEPTION
        );

        $this->conn->exec(
            "TRUNCATE TABLE students RESTART IDENTITY CASCADE"
        );
    }

    public function testAddAndGet(): void
    {
        $id = Students::addStudent(
            $this->conn,
            "Amel",
            15
        );

        $student = Students::getStudent($this->conn, $id);

        $this->assertNotNull($student);
        $this->assertSame("Amel", $student['name']);
        $this->assertEquals(15, $student['grade']);
    }

    public function testGetMissing(): void
    {
        $student = Students::getStudent($this->conn, 999);

        $this->assertNull($student);
    }

    public function testAverage(): void
    {
        Students::addStudent($this->conn, "Amel", 10);
        Students::addStudent($this->conn, "Karim", 14);

        $this->assertEquals(
            12.0,
            Students::averageGrade($this->conn)
        );
    }
}
