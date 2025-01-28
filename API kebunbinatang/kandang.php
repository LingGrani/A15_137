<?php
include_once "koneks.php";

// CREATE
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = file_get_contents("php://input");
    
    $data = json_decode($input, true);
    
    if (isset($data['idHewan'], $data['kapasitas'], $data['lokasi'])) {
        $idHewan = $data['idHewan'];
        $kapasitas = $data['kapasitas'];
        $lokasi = $data['lokasi'];

        $query = "INSERT INTO kandang (idHewan, kapasitas, lokasi) 
                  VALUES ($idHewan, $kapasitas, '$lokasi')";

        $query = $conn->prepare("INSERT INTO kandang (idHewan, kapasitas, lokasi) VALUES (?, ?, ?)");
        $query->bind_param("iis", $idHewan, $kapasitas, $lokasi);

        if ($query->execute()) {
            echo json_encode((object)['message' => 'Data berhasil ditambahkan']);
        } else {
            echo json_encode((object)['error' => 'Failed to insert data']);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "Parameter yang diperlukan tidak ada."]);
    }
}  
// READ (List)
if ($_SERVER['REQUEST_METHOD'] === 'GET' && !isset($_GET['idKandang'])) {
    $result = mysqli_query($conn, "SELECT * FROM kandang");
    $json = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $json[] = $row;
    }
    echo json_encode($json);
}

// READ (Get by ID)
if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['idKandang'])) {
    $idKandang = $_GET['idKandang'];
    $query = "SELECT * FROM kandang WHERE idKandang = $idKandang";
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
    // Ambil idKandang dari parameter query
    if (isset($_GET['idKandang'])) {
        $idKandang = intval($_GET['idKandang']);
    } else {
        echo json_encode((object)[
            'error' => 'Missing idKandang in query parameters'
        ]);
        http_response_code(400);
        exit;
    }

    $data = json_decode(file_get_contents("php://input"), true);

    $idHewan = isset($data['idHewan']) ? intval($data['idHewan']) : null;
    $kapasitas = isset($data['kapasitas']) ? intval($data['kapasitas']) : null;
    $lokasi = isset($data['lokasi']) ? $data['lokasi'] : null;

    if (is_null($idHewan) || is_null($kapasitas) || is_null($lokasi)) {
        echo json_encode((object)[
            'error' => 'Missing required fields'
        ]);
        http_response_code(400);
        exit;
    }

    $query = $conn->prepare("
        UPDATE kandang 
        SET idHewan = ?, kapasitas = ?, lokasi = ? 
        WHERE idKandang = ?
    ");
    $query->bind_param("iisi", $idHewan, $kapasitas, $lokasi, $idKandang);

    if ($query->execute()) {
        if ($query->affected_rows > 0) {
            echo json_encode((object)[
                'message' => 'Data kandang berhasil diperbarui'
            ]);
        } else {
            echo json_encode((object)[
                'error' => 'ID Kandang tidak ditemukan atau tidak ada perubahan data'
            ]);
            http_response_code(404);
        }
    } else {
        echo json_encode((object)[
            'error' => 'Failed to update kandang data'
        ]);
        http_response_code(500);
    }
}

// DELETE
if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    if (isset($_GET['idKandang'])) {
        $idKandang = intval($_GET['idKandang']);

        $query = "DELETE FROM kandang WHERE idKandang = $idKandang";
        if (mysqli_query($conn, $query)) {
            if (mysqli_affected_rows($conn) > 0) {
                echo json_encode((object)[
                    'message' => 'Data berhasil dihapus'
                ]);
            } else {
                echo json_encode((object)[
                    'error' => 'ID tidak ditemukan'
                ]);
                http_response_code(404);
            }
        } else {
            echo json_encode((object)[
                'error' => 'Gagal menghapus data'
            ]);
            http_response_code(500);
        }
    } else {
        echo json_encode((object)[
            'error' => 'idKandang diperlukan'
        ]);
        http_response_code(400);
    }
}

mysqli_close($conn);
?>
