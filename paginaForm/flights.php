<!DOCTYPE html>
<html>
    <title>Flights</title>
    <head>
    <meta charset="UTF-8">
	
  
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">

	<link rel="stylesheet" href="../HomePage/css/icomoon.css">
	<link rel="stylesheet" href="../HomePage/css/style.css">
	
    <link rel="stylesheet" href="css/TableCSS.css">
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/superfish.css">
	
    <link rel="stylesheet" href="css/style.css">
    
    <link rel="icon" type="image/png" href="Pagamento/images/icons/Cattura.ico"/>

        <style>
            
            a.my_button:link, a.my_button:visited {
                background-color: grey;
                color: white;
                padding: 14px 25px;
                text-align: center; 
                text-decoration: none;
                display: inline-block;
            }

            a.my_button:hover, a.my_button:active {
                background-color: red;
            }
        
        </style>

    </head>
    <body>
        <?php
                $dbconn = pg_connect("host=localhost port=5432 dbname=ProgettoLTW user=postgres password=Lulic71.") or die('Could not connect: '.pg_last_error());
                if(!(isset($_POST['Check']))) {
                    header("Location: ../HomePage/index.html");
                }
                else {
                    $departureCity = $_POST['dC'];
                    $departureDate = $_POST['dD'];
                    $returnDate = $_POST['rD'];
                    $continentalArea = $_POST['cA'];
                    $temp1 = $_POST['t1'];
                    $temp2 = $_POST['t2'];
                    $oneWay = $_GET['oneWay'];
                    echo "<header id=\"fh5co-header-section\" class=\"sticky-banner\">
                                    <div class=\"container\">
                                        <div class=\"nav-header\">
                                            <a href=\"#\" class=\"js-fh5co-nav-toggle fh5co-nav-toggle dark\"><i></i></a>
                                            <h1 id=\"fh5co-logo\"><a href=\"../HomePage/index.html\"><em class=\"icon-airplane\"></em>Travel Tracker</a></h1>
                                    
                                        </div>
                                    </div>
                           </header>";
                    $result;
                    if($oneWay == 0) {
                        $q1 = "select v1.codice as p0, v1.compagnia as p1, v1.partenza as p2, v1.arrivo as p3, v1.giornopartenza as p4, v1.giornoarrivo as p5, CAST(v1.prezzo AS int) as p6, v2.codice as p7, v2.compagnia as p8, v2.partenza as p9, v2.arrivo as p10, v2.giornopartenza as p11, v2.giornoarrivo as p12, CAST(v2.prezzo AS int) as p13
                            from voli v1 join citta on v1.arrivo = nome join voli v2 on v2.partenza = v1.arrivo and v2.arrivo = v1.partenza
                            where v1.partenza = '$departureCity' and continente = '$continentalArea'  and v1.giornopartenza = '$departureDate' and $temp1 <= all(select tem
                                                                                                                                                                    from meteo
                                                                                                                                                                    where citta = v1.arrivo and giorno >= v1.giornoPartenza and giorno <= v1.giornoArrivo)
                            and $temp2 >= all(select tem
                            from meteo
                            where citta = v1.arrivo and giorno >= v1.giornoPartenza and giorno <= v1.giornoArrivo) and v2.giornoArrivo = '$returnDate'";
                        $result = pg_query($q1) or die('Query failed: ' .pg_last_error());
                        echo "<section>
                              <h1>Flights Found</h1>
                              <div class=\"tbl-header\">
                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">
                                    <thead>
                                        <tr>
                                            <th>Departure Plane Code</th>
                                            <th>Departure Airline</th>
                                            <th>Departure City</th>
                                            <th>Arrival City</th>
                                            <th>Departure Date</th>
                                            <th>Arrival Date</th>
                                            <th>Price</th>
                                            <th>Return Plane Code</th>
                                            <th>Return Airline</th>
                                            <th>Departure City</th>
                                            <th>Arrival City</th>
                                            <th>Departure Date</th>
                                            <th>Arrival Date</th>
                                            <th colspan=\"2\">Price</th>
                                        </tr>
                                    </thead>
                                </table>
                              </div>
                              <div class=\"tbl-content\">
                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">
                                    <tbody>\n";
                        while($line = pg_fetch_array($result, null, PGSQL_ASSOC)) {
                            echo "<tr>\n";
                            foreach($line as $col_value) {
                                echo "<td>$col_value</td>";
                            }
                            echo "<td><a class=\"my_button\" href=\"Pagamento/Pagamento.php?oneWay=0&p0=$line[p0]&p1=$line[p1]&p2=$line[p2]&p3=$line[p3]&p4=$line[p4]&p5=$line[p5]&p6=$line[p6]&p7=$line[p7]&p8=$line[p8]&p9=$line[p9]&p10=$line[p10]&p11=$line[p11]&p12=$line[p12]&p13=$line[p13]\">Select</a></td>\n";
                            echo "</tr>\n";
                        }
                        echo "</tbody>
                              </table>
                              </div>
                              </section>";
                    }
                    else {
                        $q1 = "select v1.codice as p0, v1.compagnia as p1, v1.partenza as p2, v1.arrivo as p3, v1.giornopartenza as p4, v1.giornoarrivo as p5, CAST(v1.prezzo AS int) as p6
                               from voli v1 join citta on v1.arrivo = nome
                               where v1.partenza = '$departureCity' and continente = '$continentalArea'  and v1.giornopartenza = '$departureDate' and $temp1 <= all(select tem
                                                                                                                                                                    from meteo
                                                                                                                                                                    where citta = v1.arrivo and giorno >= v1.giornoPartenza and giorno <= v1.giornoArrivo)
                               and $temp2 >= all(select tem
                                                 from meteo
                                                 where citta = v1.arrivo and giorno >= v1.giornoPartenza and giorno <= v1.giornoArrivo)";
                        $result = pg_query($q1) or die('Query failed: ' .pg_last_error());
                        echo "<section>
                              <h1>Flights Found</h1>
                              <div class=\"tbl-header\">
                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">
                                    <thead>
                                        <tr>
                                            <th>Plane Code</th>
                                            <th>Airline</th>
                                            <th>Departure City</th>
                                            <th>Arrival City</th>
                                            <th>Departure Date</th>
                                            <th>Arrival Date</th>
                                            <th colspan=\"2\">Price</th>
                                        </tr>
                                    </thead>
                                </table>
                              </div>
                              <div class=\"tbl-content\">
                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">
                                    <tbody>\n";
                        while($line = pg_fetch_array($result, null, PGSQL_ASSOC)) {
                            echo "<tr>\n";
                            foreach($line as $col_value) {
                                echo "<td>$col_value</td>";
                            }
                            echo "<td><a class=\"my_button\" href=\"Pagamento/Pagamento.php?oneWay=1&p0=$line[p0]&p1=$line[p1]&p2=$line[p2]&p3=$line[p3]&p4=$line[p4]&p5=$line[p5]&p6=$line[p6]\">Select</a></td>\n";
                        }
                        echo "</tbody>
                              </table>
                              </div>
                              </section>";

                    }
                    pg_free_result($result);
                    pg_close($dbconn);
                }
        ?>
        <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
        <script  src="js/index.js"></script>
    </body>
</html>

