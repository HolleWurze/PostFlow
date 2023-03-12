function addClient() {
    const firstName = document.getElementById("first-name").value;
    const lastName = document.getElementById("last-name").value;

    const data = {
        firstName: firstName,
        lastName: lastName
    };

    fetch("/clients", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                // клиент успешно добавлен, обновляем страницу
                window.location.reload();
            } else {
                // обработка ошибки
            }
        })
        .catch(error => {
            console.error("Ошибка при добавлении клиента: ", error);
        });
}

const form = document.getElementById("add-client-form");
form.addEventListener("submit", event => {
    event.preventDefault();
    addClient();
});