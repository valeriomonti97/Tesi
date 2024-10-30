<!doctype html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>Confirm Payment</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">

    <link href="css/cover.css" rel="stylesheet">

    <link rel="icon" type="image/png" href="../images/icons/Cattura.ico"/>
  </head>

  <body class="text-center" background="download.jpg">
      <?php
        session_start();
        if(!(isset($_POST['Check']))) {
            header("Location: ../../../HomePage/index.html");
        }
        else {
          $nome_mittente = "Proietti Monti";
          $mail_mittente = "lorenzoproietti16@gmail.com";
          $mail_destinatario = $_POST['email'];
          $mail_oggetto = "Payment Summary";
          $mail_headers = "From: " .  $nome_mittente . " <" .  $mail_mittente . ">\r\n";
          $mail_headers .= "Reply-To: " .  $mail_mittente . "\r\n";
          $mail_headers .= "X-Mailer: PHP/" . phpversion(). "\r\n";
          $mail_headers .= "MIME-Version: 1.0\r\n";
          $mail_headers .= "Content-type: text/html; charset=iso-8859-1";
          $mail_corpo = '
            <!DOCTYPE html>
            <html>
              <head>
                <title>Payment Summary</title>
                <style>
                  table {
                    font-family: "Helvetica Neue", Helvetica, sans-serif
                  }
  
                  caption {
                    text-align: left;
                    color: silver;
                    font-weight: bold;
                    text-transform: uppercase;
                    padding: 5px;
                  }
    
                  thead {
                    background: SteelBlue;
                    color: white;
                  }
                  
                  th,td {
                    padding: 5px 10px;
                  }
                  
                  tbody tr:nth-child(even) {
                    background: WhiteSmoke;
                  }
                  
                  tbody tr td:nth-child(2) {
                    text-align:center;
                  }
                  
                  tbody tr td:nth-child(3),
                  tbody tr td:nth-child(4) {
                    text-align: right;
                    font-family: monospace;
                  }
                  
                  tfoot {
                    background: SeaGreen;
                    color: white;
                    text-align: right;
                  }
                  
                  tfoot tr th:last-child {
                    font-family: monospace;
                  }
                </style>
              </head>
              <body>
                This is the payment summary for ' . $_POST['first-name'] . ' ' . $_POST['last-name'] . ' with ' . $_GET['cardType'] . ' from Travel Traker performed the day ' . $_GET['giorno'] . ' ' . $_GET['data'] . ' at ' . $_GET['ora'] . ':' .
                '<table>
                  <caption>Payment Summary</caption>
                  <thead>
                    <tr>
                      <th>Plane Code</th>
                      <th>Airline</th>
                      <th>Departure City</th>
                      <th>Arrival City</th>
                      <th>Departure Date</th>
                      <th>Arrival Date</th>
                      <th>Price</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>' . $_SESSION['codiceAereoPartenza'] . '</td>
                      <td>' . $_SESSION['compagniaPartenza'] . '</td>
                      <td>' . $_SESSION['cittàPartenza1'] . '</td>
                      <td>' . $_SESSION['cittàArrivo1'] . '</td>
                      <td>' . $_SESSION['partenza1'] . '</td>
                      <td>' .$_SESSION['arrivo1'] . '</td>
                      <td>' . $_SESSION['prezzo1'] . '$</td>
                    </tr>';
          $total;
          if($_SESSION['oneWay'] == 0) {
            $mail_corpo .= '
                    <tr>
                      <td>' . $_SESSION['codiceAereoRitorno'] . '</td>
                      <td>' . $_SESSION['compagniaRitorno'] . '</td>
                      <td>' . $_SESSION['cittàPartenza2'] . '</td>
                      <td>' . $_SESSION['cittàArrivo2'] . '</td>
                      <td>' . $_SESSION['partenza2'] . '</td>
                      <td>' . $_SESSION['arrivo2'] . '</td>
                      <td>' . $_SESSION['prezzo2'] . '$</td>
                    </tr>';
            $total = (string)(intval($_SESSION['prezzo1']) + intval($_SESSION['prezzo2']));
          }
          else $total = $_SESSION['prezzo1'];
          $mail_corpo .= '
                  </tbody>
                  <tfoot>
                    <tr>
                      <th colspan="6">Grand Total</th>
                      <th>' .  $total . '$</th>
                    </tr>
                  </tfoot>
                </table>
              </body>
            </html>';
          $_SESSION = array();
          session_destroy();
        }
        ?>

    <div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">

      <main role="main" class="inner cover">
        <h1 class="cover-heading">Payment Executed</h1>
        <p class="lead">The payment was successful, an email was sent to the address entered for the summary</p>
        <p class="lead">
          <a href="../../../HomePage/index.html" class="btn btn-lg btn-secondary">Back to HomePage</a>
        </p>
      </main>


    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script>window.jQuery || document.write('<script src="js/jquery-slim.min.js"><\/script>')</script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>
