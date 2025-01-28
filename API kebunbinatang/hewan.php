<?php
include_once "koneks.php";

// CREATE
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents("php://input"), true);

    // Inisialisasi variabel
    $namaHewan = isset($data['namaHewan']) ? $data['namaHewan'] : null;
    $tipePakan = isset($data['tipePakan']) ? $data['tipePakan'] : null;
    $populasi = isset($data['populasi']) ? $data['populasi'] : null;
    $zonaWilayah = isset($data['zonaWilayah']) ? $data['zonaWilayah'] : null;

    // Validasi untuk mendeteksi data yang hilang
    $missingFields = [];
    if (is_null($namaHewan)) $missingFields[] = 'namaHewan';
    if (is_null($tipePakan)) $missingFields[] = 'tipePakan';
    if (is_null($populasi)) $missingFields[] = 'populasi';
    if (is_null($zonaWilayah)) $missingFields[] = 'zonaWilayah';

    // Jika ada data yang hilang, kirimkan respons error
    if (!empty($missingFields)) {
        echo json_encode((object)[
            'error' => 'Missing fields',
            'missing' => $missingFields
        ]);
        exit; // Hentikan proses jika ada data yang hilang
    }

    // Jika semua data ada, jalankan query
    $query = $conn->prepare("INSERT INTO hewan (namaHewan, tipePakan, populasi, zonaWilayah) VALUES (?, ?, ?, ?)");
    $query->bind_param("ssis", $namaHewan, $tipePakan, $populasi, $zonaWilayah);

    if ($query->execute()) {
        echo json_encode((object)['message' => 'Data berhasil ditambahkan']);
    } else {
        echo json_encode((object)['error' => 'Failed to insert data']);
    }
}


// READ (List)
if ($_SERVER['REQUEST_METHOD'] === 'GET' && !isset($_GET['idHewan'])) {
    $result = mysqli_query($conn, "SELECT * FROM hewan");
    $json = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $json[] = $row;
    }
    echo json_encode($json);
}

// READ (Get by ID)
if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['idHewan'])) {
    $idHewan = $_GET['idHewan'];
    $query = "SELECT * FROM hewan WHERE idHewan = $idHewan";
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
    if (isset($_GET['idHewan'])) {
        $idHewan = intval($_GET['idHewan']);
    } else {
        echo json_encode((object)[
            'error' => 'Missing idHewan in query parameters'
        ]);
        http_response_code(400);
        exit;
    }

    $data = json_decode(file_get_contents("php://input"), true);

    $namaHewan = isset($data['namaHewan']) ? $data['namaHewan'] : null;
    $tipePakan = isset($data['tipePakan']) ? $data['tipePakan'] : null;
    $populasi = isset($data['populasi']) ? intval($data['populasi']) : null;
    $zonaWilayah = isset($data['zonaWilayah']) ? $data['zonaWilayah'] : null;

    $missingFields = [];
    if (is_null($namaHewan)) $missingFields[] = 'namaHewan';
    if (is_null($tipePakan)) $missingFields[] = 'tipePakan';
    if (is_null($populasi)) $missingFields[] = 'populasi';
    if (is_null($zonaWilayah)) $missingFields[] = 'zonaWilayah';

    if (!empty($missingFields)) {
        echo json_encode((object)[
            'error' => 'Missing fields',
            'missing' => $missingFields
        ]);
        http_response_code(400);
        exit;
    }

    // Update data di database
    $query = $conn->prepare("
        UPDATE hewan 
        SET namaHewan = ?, tipePakan = ?, populasi = ?, zonaWilayah = ? 
        WHERE idHewan = ?
    ");
    $query->bind_param("ssisi", $namaHewan, $tipePakan, $populasi, $zonaWilayah, $idHewan);

    if ($query->execute()) {
        if ($query->affected_rows > 0) {
            echo json_encode((object)[
                'message' => 'Data berhasil diperbarui'
            ]);
        } else {
            echo json_encode((object)[
                'error' => 'ID tidak ditemukan atau tidak ada perubahan data'
            ]);
            http_response_code(404);
        }
    } else {
        echo json_encode((object)[
            'error' => 'Failed to update data'
        ]);
        http_response_code(500);
    }
}

// DELETE
if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    // Periksa apakah idHewan diberikan di query string
    if (isset($_GET['idHewan'])) {
        $idHewan = intval($_GET['idHewan']);

        // Jalankan query untuk menghapus data
        $query = "DELETE FROM hewan WHERE idHewan = $idHewan";
        if (mysqli_query($conn, $query)) {
            // Jika penghapusan berhasil
            if (mysqli_affected_rows($conn) > 0) {
                echo json_encode((object)[
                    'message' => 'Data berhasil dihapus'
                ]);
            } else {
                // Jika idHewan tidak ditemukan
                echo json_encode((object)[
                    'error' => 'ID tidak ditemukan'
                ]);
                http_response_code(404);
            }
        } else {
            // Jika terjadi error dalam eksekusi query
            echo json_encode((object)[
                'error' => 'Gagal menghapus data'
            ]);
            http_response_code(500);
        }
    } else {
        // Jika idHewan tidak diberikan
        echo json_encode((object)[
            'error' => 'idHewan diperlukan'
        ]);
        http_response_code(400);
    }
}

mysqli_close($conn);
?>
