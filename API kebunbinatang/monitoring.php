<?php
include_once "koneks.php";

// CREATE
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents("php://input"), true);

    // Inisialisasi variabel
    $idPetugas = isset($data['idPetugas']) ? $data['idPetugas'] : null;
    $idKandang = isset($data['idKandang']) ? $data['idKandang'] : null;
    $tanggalMonitoring = isset($data['tanggalMonitoring']) ? $data['tanggalMonitoring'] : null;
    $hewanSakit = isset($data['hewanSakit']) ? $data['hewanSakit'] : null;
    $hewanSehat = isset($data['hewanSehat']) ? $data['hewanSehat'] : null;
    $status = isset($data['status']) ? $data['status'] : null;

    // Validasi untuk mendeteksi data yang hilang
    $missingFields = [];
    if (is_null($idPetugas)) $missingFields[] = 'idPetugas';
    if (is_null($idKandang)) $missingFields[] = 'idKandang';
    if (is_null($tanggalMonitoring)) $missingFields[] = 'tanggalMonitoring';
    if (is_null($hewanSakit)) $missingFields[] = 'hewanSakit';
    if (is_null($hewanSehat)) $missingFields[] = 'hewanSehat';
    if (is_null($status)) $missingFields[] = 'status';

    // Jika ada data yang hilang, kirimkan respons error
    if (!empty($missingFields)) {
        echo json_encode((object)[
            'error' => 'Missing fields',
            'missing' => $missingFields
        ]);
        exit; // Hentikan proses jika ada data yang hilang
    }

    // Jika semua data ada, jalankan query
    $query = $conn->prepare("INSERT INTO monitoring (idPetugas, idKandang, tanggalMonitoring, hewanSakit, hewanSehat, status) VALUES (?, ?, ?, ?, ?, ?)");
    $query->bind_param("iisiis", $idPetugas, $idKandang, $tanggalMonitoring, $hewanSakit, $hewanSehat, $status);

    if ($query->execute()) {
        echo json_encode((object)['message' => 'Data berhasil ditambahkan']);
    } else {
        echo json_encode((object)['error' => 'Failed to insert data']);
    }
}

// READ (List)
if ($_SERVER['REQUEST_METHOD'] === 'GET' && !isset($_GET['idMonitoring'])) {
    $result = mysqli_query($conn, "SELECT * FROM monitoring");
    $json = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $json[] = $row;
    }
    echo json_encode($json);
}

// READ (Get by ID)
if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['idMonitoring'])) {
    $idMonitoring = $_GET['idMonitoring'];
    $query = "SELECT * FROM monitoring WHERE idMonitoring = $idMonitoring";
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
    if (isset($_GET['idMonitoring'])) {
        $idMonitoring = intval($_GET['idMonitoring']);
    } else {
        echo json_encode((object)[
            'error' => 'Missing idMonitoring in query parameters'
        ]);
        http_response_code(400);
        exit;
    }

    $data = json_decode(file_get_contents("php://input"), true);

    $idPetugas = isset($data['idPetugas']) ? $data['idPetugas'] : null;
    $idKandang = isset($data['idKandang']) ? $data['idKandang'] : null;
    $tanggalMonitoring = isset($data['tanggalMonitoring']) ? $data['tanggalMonitoring'] : null;
    $hewanSakit = isset($data['hewanSakit']) ? $data['hewanSakit'] : null;
    $hewanSehat = isset($data['hewanSehat']) ? $data['hewanSehat'] : null;
    $status = isset($data['status']) ? $data['status'] : null;

    $query = $conn->prepare("UPDATE monitoring SET idPetugas = ?, idKandang = ?, tanggalMonitoring = ?, hewanSakit = ?, hewanSehat = ?, status = ? WHERE idMonitoring = ?");
    $query->bind_param("iisiisi", $idPetugas, $idKandang, $tanggalMonitoring, $hewanSakit, $hewanSehat, $status, $idMonitoring);

    if ($query->execute()) {
        echo json_encode((object)['message' => 'Data berhasil diperbarui']);
    } else {
        echo json_encode((object)['error' => 'Failed to update data']);
        http_response_code(500);
    }
}

// DELETE
if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    if (isset($_GET['idMonitoring'])) {
        $idMonitoring = intval($_GET['idMonitoring']);

        $query = "DELETE FROM monitoring WHERE idMonitoring = $idMonitoring";
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
        echo json_encode((object)['error' => 'idMonitoring diperlukan']);
        http_response_code(400);
    }
}

mysqli_close($conn);
?>
