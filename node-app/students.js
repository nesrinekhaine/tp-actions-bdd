const { Pool } = require('pg');

async function addStudent(db, name, grade) {
    const result = await db.query(
        'INSERT INTO students (name, grade) VALUES ($1, $2) RETURNING id',
        [name, grade]
    );
    return result.rows[0].id;
}

async function getStudent(db, id) {
    const result = await db.query(
        'SELECT * FROM students WHERE id = $1',
        [id]
    );
    return result.rows[0] || null;
}

async function averageGrade(db) {
    const result = await db.query(
        'SELECT AVG(grade) AS average FROM students'
    );
    return result.rows[0].average === null
        ? null
        : Number(result.rows[0].average);
}

module.exports = { addStudent, getStudent, averageGrade };
