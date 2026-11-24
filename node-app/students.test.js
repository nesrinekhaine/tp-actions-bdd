const { Client } = require('pg');

const {
    addStudent,
    getStudent,
    averageGrade
} = require('./students');

let db;

beforeAll(async () => {
    db = new Client({
        host: 'localhost',
        user: 'postgres',
        password: 'postgres',
        database: 'tp',
        port: 5432
    });

    await db.connect();
});

beforeEach(async () => {
    await db.query(
        'TRUNCATE TABLE students RESTART IDENTITY CASCADE'
    );
});

afterAll(async () => {
    await db.end();
});

test('Ajouter et lire un étudiant', async () => {
    const id = await addStudent(db, 'Amel', 15);

    const student = await getStudent(db, id);

    expect(student.name).toBe('Amel');
    expect(Number(student.grade)).toBe(15);
});

test('Lire un étudiant inexistant', async () => {
    const student = await getStudent(db, 999);

    expect(student).toBeNull();
});

test('Calculer la moyenne', async () => {
    await addStudent(db, 'Amel', 10);
    await addStudent(db, 'Karim', 14);

    const average = await averageGrade(db);

    expect(average).toBe(12);
});
