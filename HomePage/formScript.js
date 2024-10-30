var map = {"hong kong": "Cina", "roma": "Italia", "milano": "Italia", "londra": "Inghilterra", "singapore": "Repubblica di Singapore", "istanbul": "Turchia", "seul": "Corea del Sud", "miami": "USA", "la mecca": "Arabia Saudita", "praga": "Repubblica Ceca", "guangzhou": "Cina", "taipei": "Taiwan", "tokyo": "Giappone", "phuket": "Thailandia", "kuala lumpur": "Malaysia", "shenzhen": "Cina", "new york": "USA", "parigi": "Francia", "dubai": "Emirati Arabi Uniti", "macao": "Cina", "bangkok": "Thailandia", "nizza": "Francia", "bucarest": "Romania", "cracovia": "Polonia", "lisbona": "Portogalllo", "monaco di baviera": "Germania", "burgas": "Bulgaria", "bruxelles": "Belgio", "san pietroburgo": "Russia", "dublino": "Irlanda", "varsavia": "Polonia", "madrid": "Spagna", "firenze": "Italia", "budapest": "Ungheria", "berlino": "Germania", "sofia": "Bulgaria", "venezia": "Italia", "vienna": "Austria", "amsterdam": "Olanda", "mosca": "Russia", "barcellona": "Spagna", "delhi": "India", "mumbai": "India", "pattaya": "Thailandia", "las vegas": "USA", "adalia": "Turchia", "los angeles": "USA", "cancun": "Messico", "osaka": "Giappone", "agra": "India", "ho chi minh city": "India", "johannesburg": "Sudafrica", "orlando": "USA", "riyadh": "Arabia Saudita", "johor bahru": "Malaysia", "chennai": "India", "atene": "Grecia", "jaipur": "India", "pechino": "Cina", "denpasar": "Indonesia", "toronto": "Canada",  "hanoi": "Vietnam", "sydney": "Australia", "san francisco": "USA", "ha long": "Vietnam", "punta cana": "Repubblica Dominicana", "dammam": "Arabia Saudita", "zhuhai": "Cina", "cairo": "Egitto", "penang island": "Malaysia", "doha": "Qatar", "copenaghen": "Danimarca", "candia": "Grecia", "gerusalemme": "Israele", "edirne": "Turchia", "phnom penh": "Cambogia", "jeju": "Corea del Sud", "kyoto": "Giappone", "chiang mai": "Thailandia", "honolulu": "USA", "melbourne": "Australia", "tel aviv": "Israele", "marrakech": "Marocco", "auckland": "Nuova Zelanda", "vancouver": "Canada", "giacarta": "Indonesia", "francoforte": "Germania", "artvin": "Turchia", "guilin": "Cina", "stoccolma": "Svezia", "rio de janeiro": "Brasile", "calcutta": "India", "buenos aires": "Argentina", "Chiba": "Giappone", "siem reap": "Cambogia", "città del messico": "Messico", "lima": "Perù", "taichung": "Taiwan", "rodi": "Grecia", "washington dc": "USA", "abu dhabi": "Emirati Arabi Uniti", "colombo": "Sri Lanka", "paignton": "Inghilterra"};

function validaForm() {
    if(document.getElementById("departureCity").value == "") {
        _alert("Error", "Valid departure city is required.");
        return false;
    }
    if(document.getElementById("departureDate").value == "") {
        _alert("Error", "Valid departure date is required.");
        return false;
    }
    if(document.getElementById("returnDate").value == "") {
        _alert("Error", "Valid return date is required.");
        return false;
    }
    if(document.getElementById("temp1").value == "") {
        _alert("Error", "Please enter the desired minimum temperature.");
        return false;
    }
    if(document.getElementById("temp2").value == "") {
        _alert("Error", "Please enter the desired maximum temperature.");
        return false;
    }
    if(new Date(document.getElementById("departureDate").value).getTime() > new Date(document.getElementById("returnDate").value).getTime()) {
        _alert("Error", "The return date must be later than the departure date");
        return false;
    }
    if(document.getElementById("continentalArea").value == "None") {
        _alert("Error", "Enter a continental area");
        return false;
    }
    if(parseInt(document.getElementById("temp1").value) > parseInt(document.getElementById("temp2").value)) {
        _alert("Error", "The minimum temperature must be less than or equal to the maximum temperature");
        return false;
    }
    var oneWay;
    if(document.getElementById("save-info").checked) {
        localStorage.setItem("departureCity", document.getElementById("departureCity").value);
        localStorage.setItem("departureDate", document.getElementById("departureDate").value);
        localStorage.setItem("returnDate", document.getElementById("returnDate").value);
        localStorage.setItem("temp1", document.getElementById("temp1").value);
        localStorage.setItem("temp2", document.getElementById("temp2").value);
        localStorage.setItem("continentalArea", document.getElementById("continentalArea").value);
        if(document.getElementById("oneWay").checked) localStorage.setItem("travelType", "oneWay");
        else localStorage.setItem("travelType", "roundtrip");
    }
    if(document.getElementById("oneWay").checked) oneWay = 1;
    else oneWay = 0;
    document.getElementById("Flights").setAttribute("action", "../paginaForm/flights.php?oneWay=" + oneWay);
    return true;
}

function infoClient() {
    if(document.getElementById("save-info").checked) _alert("Info", "You have selected to save this information for the next time");
    else _alert("Info", "You have selected not to save this information for the next time");
    return true;
}

function sectFields() {
    if(localStorage.getItem("departureCity") != null) {
        document.getElementById("departureCity").value = localStorage.getItem("departureCity");
        document.getElementById("departureDate").value = localStorage.getItem("departureDate");
        document.getElementById("returnDate").value = localStorage.getItem("returnDate");
        document.getElementById("temp1").value = localStorage.getItem("temp1");
        document.getElementById("temp2").value = localStorage.getItem("temp2");
        document.getElementById("continentalArea").value = localStorage.getItem("continentalArea");
        document.getElementById(localStorage.getItem("travelType")).checked = true;
        disableFields();
    }
    return true;
}

function searchCity() {
    var input = document.getElementById("search").value.toLowerCase();
    if(map[input] != undefined) {
        localStorage.city = input;
        window.location.href = 'City.html';
    }
    else _alert("Error", "The city entered is not present among the available destinations");
}

function  setHref() {
    var ref = document.getElementById("ref");
    var input = document.getElementById("search").value.toLowerCase();
    if(map[input] != undefined) {
        localStorage.city = input;
        ref.href = 'City.html';
        return true;
    }
    ref.href = "";
    _alert("Error", "The city entered is not present among the available destinations");
    return false;
}

function sectAttributes() {
    var city = localStorage.city;
    document.getElementById("h1").setAttribute("data-title", city[0].toUpperCase() + city.substring(1, city.length));
    document.getElementById("h1").innerText = city[0].toUpperCase() + city.substring(1, city.length);
    document.getElementById("imageB").setAttribute("style", "background-image: url(images/" + city.replace(" ", "") + ".jpg)");
    document.getElementById("country").innerText = "Nazione: " + map[city];
}

function sectNYC() {
    localStorage.city = "new york";
}

function sectParis() {
    localStorage.city = "parigi";
}

function sectRome() {
    localStorage.city = "roma";
}

function disableFields() {
    var returnDate = document.getElementById("returnDate");
    if(document.getElementById("oneWay").checked) returnDate.disabled = true;
    else returnDate.disabled = false;
}