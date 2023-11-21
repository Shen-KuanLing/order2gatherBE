function submitForm() {
    var UID = document.getElementById("UID").value;
    var HID = document.getElementById("HID").value;
    var OID = document.getElementById("OID").value;
    var content = document.getElementById("content").value;
    
    // Send data to the backend
    fetch('/report', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ 
            UID: UID, 
            HID: HID, 
            OID: OID, 
            content: content
        })
    })
    .then(response => response.json())
    .then(data => {
        // Handle the response from the backend
        console.log(data);
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
