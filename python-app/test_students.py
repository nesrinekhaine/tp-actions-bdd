import pytest
import psycopg2
from students import add_student, get_student, average_grade

# Fonction utilitaire pour créer une connexion et nettoyer la table avant chaque test
def get_db_connection():
    conn = psycopg2.connect(
        host="localhost",
        database="tp",
        user="postgres",
        password="postgres"
    )
    with conn.cursor() as cursor:
        cursor.execute("DELETE FROM students;")
    conn.commit()
    return conn

def test_add_and_get_student():
    """Test 1: Ajouter un étudiant, puis le relire."""
    conn = get_db_connection()
    try:
        add_student(conn, "Amel", 14)
        student = get_student(conn, "Amel")
        assert student is not None
        assert student[0] == "Amel"
        assert float(student[1]) == 14.0
    finally:
        conn.close()

def test_get_nonexistent_student():
    """Test 2: Lire un étudiant qui n'existe pas (doit retourner None)."""
    conn = get_db_connection()
    try:
        student = get_student(conn, "Inconnu")
        assert student is None
    finally:
        conn.close()

def test_moyenne():
    """Test 3: Ajouter deux étudiants et vérifier la moyenne (10 et 14 -> 12)."""
    conn = get_db_connection()
    try:
        add_student(conn, "Amel", 10)
        add_student(conn, "Karim", 14)
        assert average_grade(conn) == 12.0
    finally:
        conn.close()
