<!DOCTYPE html>
<html>
<head>
	
	<title>Payment</title>
	
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link rel="icon" type="image/png" href="images/icons/Cattura.ico"/>
	<link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
	<link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
	<link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
	<link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
	<link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
	<link rel="stylesheet" type="text/css" href="vendor/daterangepicker/daterangepicker.css">
	<link rel="stylesheet" type="text/css" href="css/util.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
	
	<style>
		
		form#Payment ol li label {
            background:none no-repeat left 50%;
            line-height: 20px;
            padding: 0 0 0 30px;
            width: auto;
        }
		form#Payment label[for=visa] {
            background-image: url("images/Visa.png");
        }
        form#Payment label[for=amex] {
            background-image: url("images/AmEx.png");
        }
        form#Payment label[for=mastercard] {
            background-image: url("images/MasterCard.png");
        }
        form#Payment label[for=paypal] {
            background-image: url("images/PayPal.png");
        }
		form#Payment ol li {
			padding: 10px;
		}
	
	</style>

	<script>

		function setAction() {
			var cardType;
			if(document.getElementById("visa").checked) cardType = "Visa";
			else if(document.getElementById("amex").checked) cardType = "Amex";
			else if(document.getElementById("mastercard").checked) cardType = "MasterCard";
			else cardType = " PayPal";
			new Date();
			dat = new Date();
			gg = dat.getDate();
			mm = (dat.getMonth() + 1);
			aa = dat.getFullYear();
			data_corr= gg + "-" + mm + "-" + aa;
			hh = dat.getHours();
			mn = dat.getMinutes();
			ora_corr= hh + ":" + mn;
			giorno = dat.getDay();
			if (giorno == 1) {
				sett = "Monday";
			} else if (giorno == 2) {
				sett = "Tuesday";
			} else if (giorno == 3) {
				sett = "Wednesday";
			} else if (giorno == 4) {
				sett = "Thursday";
			} else if (giorno == 5) {
				sett = "Friday";
			} else if (giorno == 6) {
				sett = "Saturday";
			} else if (giorno == 0) {
				sett = "Sunday";
			}
			document.getElementById("Payment").setAttribute("action", "Conferma/Conferma.php?cardType=" + cardType + "&giorno=" + sett + "&data=" + data_corr + "&ora=" + ora_corr);
		}

	</script>
	
</head>
<body>

    <?php
        session_start();
        $_SESSION['codiceAereoPartenza'] = $_GET['p0'];
        $_SESSION['compagniaPartenza'] = $_GET['p1'];
        $_SESSION['cittàPartenza1'] = $_GET['p2'];
        $_SESSION['cittàArrivo1'] = $_GET['p3'];
        $_SESSION['partenza1'] = $_GET['p4'];
        $_SESSION['arrivo1'] = $_GET['p5'];
		$_SESSION['prezzo1'] = $_GET['p6'];
		if($_GET['oneWay'] == 0) {
			$_SESSION['codiceAereoRitorno'] = $_GET['p7'];
			$_SESSION['compagniaRitorno'] = $_GET['p8'];
			$_SESSION['cittàPartenza2'] = $_GET['p9'];
			$_SESSION['cittàArrivo2'] = $_GET['p10'];
			$_SESSION['partenza2'] = $_GET['p11'];
			$_SESSION['arrivo2'] = $_GET['p12'];
			$_SESSION['prezzo2'] = $_GET['p13'];
			$_SESSION['oneWay'] = 0;
		}
		else $_SESSION['oneWay'] = 1;
	?>

	<div class="container-contact100">
		<div class="wrap-contact100">
			<form class="contact100-form validate-form" id="Payment" method="post">
				<span class="contact100-form-title">
					Insert your data
				</span>

				<label class="label-input100" for="first-name">Insert your name *</label>
				<div class="wrap-input100 rs1-wrap-input100 validate-input" data-validate="Type first name">
					<input id="first-name" class="input100" type="text" name="first-name" placeholder="First name">
					<span class="focus-input100"></span>
				</div>
				<div class="wrap-input100 rs2-wrap-input100 validate-input" data-validate="Type last name">
					<input class="input100" type="text" name="last-name" placeholder="Last name">
					<span class="focus-input100"></span>
				</div>

				<label class="label-input100" for="email">Enter your email *</label>
				<div class="wrap-input100 validate-input" data-validate = "Valid email is required: ex@abc.xyz">
					<input id="email" class="input100" type="text" name="email" placeholder="Eg. example@email.com">
					<span class="focus-input100"></span>
				</div>

				<label class="label-input100" for="phone">Enter phone number</label>
				<div class="wrap-input100">
					<input id="phone" class="input100" type="text" name="phone" placeholder="Eg. +1 800 000000">
					<span class="focus-input100"></span>
				</div>

				<label class="label-input100" for="cardDetails">Card Details *</label>
				<div class="wrap-input100 validate-input">
					<ol id="cardDetails" name="cardDetails">
						<br>
						<li>
							<input id="visa" name="cardtype" type="radio" checked>
							<label for="visa">&nbspVisa</label>
						</li>
						<br>
						<li>
							<input id="amex" name="cardtype" type="radio">
							<label for="amex">&nbspAmEx</label>
						</li>
						<br>
						<li>
							<input id="mastercard" name="cardtype" type="radio">
							<label for="mastercard">&nbspMastercard</label>
						</li>
						<br>
						<li>
							<input id="paypal" name="cardtype" type="radio">
							<label for="paypal">&nbspPayPal</label>
						</li>
					</ol>
				</div>
				
				<label class="label-input100" for="cardNumber">Card Number *</label>
				<div class="wrap-input100 validate-input" data-validate = "Card Number is required">
					<input id="cardNumber" class="input100" name="cardNumber" type="number">
					<span class="focus-input100"></span>
				</div>

				<label class="label-input100" for="securityCode">Security Code *</label>
				<div class="wrap-input100 validate-input" data-validate="Security Code is required">
					<input id="securityCode" class="input100" name="securityCode" type="number">
					<span class="focus-input100"></span>
				</div>

				<label class="label-input100" for="nameCard">Name on Card *</label>
				<div class="wrap-input100 validate-input" data-validate = "Name on card is required">
					<input id="nameCard" class="input100" name="nameCard" type="text" placeholder="Exact name as on the card">
					<span class="focus-input100"></span>
				</div>

				<div class="container-contact100-form-btn">
					<button class="contact100-form-btn" name="Check" onClick="setAction()"> 
						Buy it!
					</button>
				</div>
			</form>

			<div class="contact100-more flex-col-c-m" style="background-image: url('images/viaggio.jpg');"> </div>
		</div>
	</div>



	<div id="dropDownSelect1"></div>

	<script src="vendor/jquery/jquery-3.2.1.min.js"></script>
	<script src="vendor/animsition/js/animsition.min.js"></script>
	<script src="vendor/bootstrap/js/popper.js"></script>
	<script src="vendor/bootstrap/js/bootstrap.min.js"></script>
	<script src="vendor/select2/select2.min.js"></script>
	<script src="vendor/daterangepicker/moment.min.js"></script>
	<script src="vendor/daterangepicker/daterangepicker.js"></script>
	<script src="vendor/countdowntime/countdowntime.js"></script>
	<script src="js/main.js"></script>

</body>
</html>