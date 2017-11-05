<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

$insert = null;
if (isset($_POST['title'])) {
    $json = $_POST;

    $title = $json['title'];
    $id = $json['id'];
    $author = $json['author'];
    $location = "," . $json['location'];
    $description = $json['description'];
    array_shift($json);
    array_shift($json);
    array_shift($json);
    array_shift($json);
    array_shift($json);

    $out = array(
        "snippets" => array()
    );

    $sizee = sizeof($json) / 2;
    for ($eye = 0; $eye < $sizee; $eye++) {
        array_push($out["snippets"], array(
            "code" => $json["code" . $eye],
            "description" => $json["desc" . $eye]
        ));
    }

//    echo '<pre>';
//    echo json_encode($out, JSON_PRETTY_PRINT);
//    echo '</pre><br><br>';

    $insert = array(
        "id" => $id,
        "date" => round(microtime(true) * 1000),
        "Title" => $title,
        "Author" => $author,
        "description" => $description,
        "snippets" => $out["snippets"]
    );
}

$file = json_decode(file_get_contents("JSON.json"), true);

echo '<h1>Tree</h1><pre>';

function getSpace($amount, $char) {
    $ret = "";
    for ($i2 = 0; $i2 < $amount; $i2++) {
        $ret .= $char;
    }
    return $ret;
}

$maxId = 0;

function checkStuff(&$searchIn, $nest, $currentPath, $insert) {
    global $maxId;
    global $location;

    if (isset($searchIn["sectionName"])) { // Is container
        $sectionName = &$searchIn["sectionName"];
        if (($currentPath . "," . $sectionName) == $location && $insert != null) {
            array_push($searchIn["snippetObjects"], $insert);
        }

        echo getSpace($nest, " ") . '+ <a href="javascript:changeLocationText(\'' . ltrim($currentPath . "," . $sectionName, ',') . '\')">' . $sectionName . '</a><br>';

        checkStuff($searchIn["snippetObjects"], $nest + 1, $currentPath . "," . $sectionName, $insert);

    } else {

        if (!isset($searchIn["Title"])) {
            $sizeee = sizeof($searchIn);

            for ($i = 0; $i < $sizeee; $i++) {
                checkStuff($searchIn[$i], $nest + 1, $currentPath, $insert);
            }
        } else {
            if ($searchIn["id"] > $maxId) {
                $maxId = $searchIn["id"];
            }
            echo getSpace($nest, " ") . '└ ' . $searchIn["Title"] . '<br>';
        }
    }
}

function checkPath($insert) {
    global $file;


    for ($i = 0; $i < sizeof($file); $i++) {
        checkStuff($file[$i], 0, "", $insert);
    }
}

checkPath($insert);

echo '</pre>';
?>

<html>
<head>
    <title>Upload Code Stuff</title>

    <style>
        table {
            width: 100%;
            padding: 20px;
        }

        .code {
            width: 100%;
        }

        .desc {
            width: 100%;
        }

        .tableTitles {
            text-align: left;
            font-size: 24px;
        }

        .rowMod {
            background-color: lightblue;
            padding: 10px;
            margin: 15px;
            cursor: pointer;
        }

        pre {
            background-color: burlywood;
            padding: 10px;
        }

        .top {
            width: 25%;
        }
    </style>

    <script src="https://code.jquery.com/jquery-3.2.1.min.js"
            integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>

    <script>
        var rows = [{
            "codeid": "code0",
            "descriptionid": "desc0"
        }];

        function addRow() {
            var id = rows.length;
            $("#holder").append("<tr><td><textarea rows=\"10\" name=\"code" + id + "\" id=\"code" + id + "\" class=\"code\">// Code here</textarea></td>" +
                "<td><textarea rows=\"10\" name=\"desc" + id + "\" id=\"desc" + id + "\" class=\"desc\">Description of code block</textarea></td></tr>");

            rows.push({
                "codeid": "code" + id,
                "descriptionid": "desc" + id
            });
        }

        function removeRow() {
            var removeId = rows.length - 1;

            $("#code" + removeId).remove();
            $("#desc" + removeId).remove();

            rows.pop();
        }

        function changeLocationText(text) {
            document.getElementById("location").value = text;
        }

    </script>
</head>
<body>

<h1>Title: <?php ?></h1>

<form action="upload.php" method="post">
    <input class="top" id="title" name="title" value="Title Of Snippet"><br>
    <input class="top" id="author" name="author" value="Your Name"><br>
    <input class="top" type="number" id="id" name="id" value="<?php echo $maxId + 1; ?>"><br>
    <input class="top" id="location" name="location" value="Root 111,Root 222"><br>
    <textarea class="top" id="description" name="description" rows="10">Description of the snippet</textarea><br>
    <br><br>
    <table id="holder">
        <tr>
            <th class="tableTitles">Code</th>
            <th class="tableTitles">Description</th>
        </tr>
        <tr>
            <td><textarea rows="10" name="code0" id="code0" class="code">// Code here</textarea></td>
            <td><textarea rows="10" name="desc0" id="desc0" class="desc">Description of code block</textarea></td>
        </tr>
    </table>

    <input class="rowMod" type="submit" value="Submit">
</form>


<button class="rowMod" onclick="addRow();">Add Row</button>
<button class="rowMod" onclick="removeRow();">Remove Row</button>

</body>
</html>