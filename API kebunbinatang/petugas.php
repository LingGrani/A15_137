<?php
include_once "koneks.php";

// CREATE
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents("php://input"), true);

    // Inisialisasi variabel
    $namaPetugas = isset($data['namaPetugas']) ? $data['namaPetugas'] : null;
    $jabatan = isset($data['jabatan']) ? $data['jabatan'] : null;

    // Validasi untuk mendeteksi data yang hilang
    if (is_null($namaPetugas) || is_null($jabatan)) {
        echo json_encode((object)['error' => 'Missing fields']);
        exit;
    }

    // Query untuk menambah data
    $query = $conn->prepare("INSERT INTO petugas (namaPetugas, jabatan) VALUES (?, ?)");
    $query->bind_param("ss", $namaPetugas, $jabatan);

    if ($query->execute()) {
        echo json_encode((object)['message' => 'Data berhasil ditambahkan']);
    } else {
        echo json_encode((object)['error' => 'Failed to insert data']);
    }
}

// READ (List)
if ($_SERVER['REQUEST_METHOD'] === 'GET' && !isset($_GET['idPetugas'])) {
    $result = mysqli_query($conn, "SELECT * FROM petugas");
    $json = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $json[] = $row;
    }
    echo json_encode($json);
}

// READ (Get by ID)
if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['idPetugas'])) {
    $idPetugas = $_GET['idPetugas'];
    $query = "SELECT * FROM petugas WHERE idPetugas = $idPetugas";
    $result = mysqli_query($conn, $query);
    if ($result) {
        $data = mysqli_fetch_assoc($result);
        echo json_encode($data);
    } else {
        echo json_encode([]);
    }
}

// UPDATE
if ($_SERVER['REQUEST_METHOD'] === 'PUT') {
    if (isset($_GET['idPetugas'])) {
        $idPetugas = intval($_GET['idPetugas']);
    } else {
        echo json_encode((object)[
            'error' => 'Missing idPetugas in query parameters'
        ]);
        http_response_code(400);
        exit;
    }

    $data = json_decode(file_get_contents("php://input"), true);

    $namaPetugas = isset($data['namaPetugas']) ? $data['namaPetugas'] : null;
    $jabatan = isset($data['jabatan']) ? $data['jabatan'] : null;

    $query = $conn->prepare("UPDATE petugas SET namaPetugas = ?, jabatan = ? WHERE idPetugas = ?");
    $query->bind_param("ssi", $namaPetugas, $jabatan, $idPetugas);

    if ($query->execute()) {
        echo json_encode((object)['message' => 'Data berhasil diperbarui']);
    } else {
        echo json_encode((object)['error' => 'Failed to update data']);
        http_response_code(500);
    }
}

// DELETE
if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    if (isset($_GET['idPetugas'])) {
        $idPetugas = intval($_GET['idPetugas']);

        $query = "DELETE FROM petugas WHERE idPetugas = $idPetugas";
        if (mysqli_query($conn, $query)) {
            if (mysqli_affected_rows($conn) > 0) {
                echo json_encode((object)['message' => 'Data berhasil dihapus']);
            } else {
                echo json_encode((object)['error' => 'ID tidak ditemukan']);
                http_response_code(404);
            }
        } else {
            echo json_encode((object)['error' => 'Failed to delete data']);
            http_response_code(500);
        }
    } else {
        echo json_encode((object)['error' => 'idPetugas diperlukan']);
        http_response_code(400);
    }
}

mysqli_close($conn);
?>
