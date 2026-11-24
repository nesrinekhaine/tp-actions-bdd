<?php

class Students
{
    public static function addStudent(
        PDO $conn,
        string $name,
        float $grade
    ): int {

        $sql = "INSERT INTO students (name, grade)
                VALUES (:name, :grade)
                RETURNING id";

        $stmt = $conn->prepare($sql);

        $stmt->execute([
            ':name' => $name,
            ':grade' => $grade
        ]);

        return (int) $stmt->fetchColumn();
    }

    public static function getStudent(
        PDO $conn,
        int $id
    ): ?array {

        $sql = "SELECT * FROM students WHERE id = :id";

        $stmt = $conn->prepare($sql);
        $stmt->execute([':id' => $id]);

        $student = $stmt->fetch(PDO::FETCH_ASSOC);

        return $student ?: null;
    }

    public static function averageGrade(PDO $conn): float
    {
        $sql = "SELECT AVG(grade) FROM students";

        $result = $conn->query($sql)->fetchColumn();

        return (float) $result;
    }
}
