def add_student(conn, name, grade):
    """Ajoute un étudiant dans la base de données."""
    with conn.cursor() as cursor:
        cursor.execute(
            "INSERT INTO students (name, grade) VALUES (%s, %s);",
            (name, grade)
        )
    conn.commit()

def get_student(conn, name):
    """Lit un étudiant par son nom."""
    with conn.cursor() as cursor:
        cursor.execute(
            "SELECT name, grade FROM students WHERE name = %s;",
            (name,)
        )
        return cursor.fetchone()

def average_grade(conn):
    """Calcule la moyenne des notes de tous les étudiants."""
    with conn.cursor() as cursor:
        cursor.execute("SELECT AVG(grade) FROM students;")
        result = cursor.fetchone()
        if result and result[0] is not None:
            return float(result[0])
        return 0.0
