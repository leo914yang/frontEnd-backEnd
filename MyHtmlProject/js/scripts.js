/*!
* Start Bootstrap - Resume v7.0.6 (https://startbootstrap.com/theme/resume)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-resume/blob/master/LICENSE)
*/
//
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {
    const port = "http://localhost:8081";

    // Activate Bootstrap scrollspy on the main nav element
    const sideNav = document.body.querySelector('#sideNav');
    if (sideNav) {
        new bootstrap.ScrollSpy(document.body, {
            target: '#sideNav',
            rootMargin: '0px 0px -40%',
        });
    };

    // Collapse responsive navbar when toggler is visible
    const navbarToggler = document.body.querySelector('.navbar-toggler');
    const responsiveNavItems = [].slice.call(
        document.querySelectorAll('#navbarResponsive .nav-link')
    );
    responsiveNavItems.map(function (responsiveNavItem) {
        responsiveNavItem.addEventListener('click', () => {
            if (window.getComputedStyle(navbarToggler).display !== 'none') {
                navbarToggler.click();
            }
        });
    });

    const dataFormCreate = document.getElementById('dataFormCreate');
    //const responseDivCreate = document.getElementById('responseCreate');
    dataFormCreate.addEventListener('submit', function (e) {
        e.preventDefault();
        const nameInput = document.getElementById('nameCreate');
        const name = nameInput.value;
        const desInput = document.getElementById('desCreate');
        const des = desInput.value;
        const idInput = document.getElementById("idCreate");
        const id = idInput.value;
        const birthInput = document.getElementById("birthCreate");
        const birth = birthInput.value;
        const phoneInput = document.getElementById("phoneCreate");
        const phone = phoneInput.value;

        const headers = {
            'Content-Type': 'application/json'
        };

        fetch(port + '/api/user', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({
                'name': name,
                'des': des,
                'id': id,
                'birth': birth,
                'phone': phone
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('發生錯誤');
                }
                return response.text();
            })
            .then(data => {
                const jsonData = JSON.parse(data);
                const token = jsonData.token;
                //const publicKeyString = jsonData.publicKey;
                localStorage.setItem('jsonData', token);
                //localStorage.setItem('publicKey', publicKeyString);
                //responseDivCreate.innerHTML = `Your key: ${publicKeyString}`;
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

    const dataFormRead = document.getElementById('dataFormRead');
    const responseDivRead = document.getElementById('responseRead');
    dataFormRead.addEventListener('submit', function (e) {
        e.preventDefault();

        const nameInput = document.getElementById('nameRead');
        const name = nameInput.value;
        const apiUrl = `/api/user?name=${name}`;

        fetch(port + apiUrl, {
            method: 'GET'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('發生錯誤');
                }
                return response.json();
            })
            .then(data => {
                let content = '';
                data.forEach(item => {
                    content += `ID: ${item.id}, Name: ${item.name}, Des: ${item.des}<br>`;
                });
                responseDivRead.innerHTML = content;
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

    const dataFormReadNews = document.getElementById('dataFormReadNews');
    const responseDivReadNews = document.getElementById('responseReadNews');
    dataFormReadNews.addEventListener('submit', function (e) {
        e.preventDefault();
        const numReadNews = document.getElementById('numReadNews');
        const apiUrl = `/api/news?name=${numReadNews.value}`;
        fetch(port + apiUrl, {
            method: 'GET'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('發生錯誤');
                }
                return response.json();
            })
            .then(data => {
                let content = '';
                for (let index = 0; index < data.length; index++) {
                    const item = data[index];
                    const url = item.url;
                    const title = item.title;
                    const id = item.id;
                    content += `${id}: <a href="${url}" target="_blank">${title}</a><br>`;
                }
                responseDivReadNews.innerHTML = content;
            })
            .catch(error => {
                console.error('Error:', error);
            });


    });

    const dataFormUpdate = document.getElementById('dataFormUpdate');
    //const responseDivUpdate = document.getElementById('responseUpdate');
    dataFormUpdate.addEventListener('submit', function (e) {
        const headers = new Headers();
        headers.append('Content-Type', 'application/json');
        const jwtToken = localStorage.getItem('jsonData');
        if (jwtToken) {
            headers.append('Authorization', `${jwtToken}`);
        }
        e.preventDefault();

        const idInput = document.getElementById('idUpdate');
        const id = idInput.value;
        const desInput = document.getElementById('desUpdate');
        const des = desInput.value;

        fetch(port + '/api/user', {
            method: 'PUT',
            headers: headers,
            body: JSON.stringify({ 'id': id, 'des': des })
        })
            .then(response => response.text())
            .then(data => {
                if (data === 'Failed') {
                    alert('Update Failed');
                } else if (data === 'Success') {
                    alert('Update Successful');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

    const dataFormDelete = document.getElementById('dataFormDelete');
    //const responseDivDelete = document.getElementById('responseDelete');
    dataFormDelete.addEventListener('submit', function (e) {
        const headers = new Headers();
        headers.append('Content-Type', 'application/json');
        const jwtToken = localStorage.getItem('jsonData');
        if (jwtToken) {
            headers.append('Authorization', `${jwtToken}`);
        }
        e.preventDefault();

        const idInput = document.getElementById('idDelete');
        const id = idInput.value;
        if (!id) {
            alert('ID cannot be empty');
            return;
        }

        fetch(port + '/api/user', {
            method: 'DELETE',
            headers: headers,
            body: JSON.stringify({ 'id': id })
        })
            .then(response => response.text())
            .then(data => {
                if (data === 'Failed') {
                    alert('Delete Failed');
                } else if (JSON.parse(data).token === jwtToken) {
                    localStorage.removeItem('jsonData');
                    localStorage.removeItem('publicKeyString');
                    const floatingWindow = document.getElementById('floating-window');
                    floatingWindow.innerHTML = 'Token has deleted!';
                    alert('Delete Successful');
                } else {
                    alert('Delete Successful');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

    const dataFormDeleteNews = document.getElementById("dataFormDeleteNews");
    dataFormDeleteNews.addEventListener('submit', function (e) {
        const headers = new Headers();
        headers.append('Content-Type', 'application/json');
        const jwtToken = localStorage.getItem('jsonData');
        if (jwtToken) {
            headers.append('Authorization', `${jwtToken}`);
        }
        e.preventDefault();

        const idDeleteNewsInput = document.getElementById("idDeleteNews");
        const id = idDeleteNewsInput.value;
        if (!id) {
            alert('ID cannot be empty');
            return;
        }
        
        fetch(port + '/api/news/${id}', {
            method: 'DELETE',
            headers: headers
        })
            .then(response => response.text())
            .then(data => {
                if (data === 'Failed') {
                    alert('Delete Failed');
                } else {
                    alert('Delete Successful');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

    const textInput = document.getElementById('NewsNum');
    const newsContent = document.getElementById('NewsContent');
    const buttonContainer = document.getElementById('button-container');

    buttonContainer.addEventListener('click', (event) => {
        if (event.target.classList.contains('button')) {
            const websiteUrl = event.target.getAttribute('data-website');
            const apiUrl = '/api/news';
            const headers = {
                'Content-Type': 'application/json'
            };
            if (websiteUrl) {
                fetch(port + apiUrl, {
                    method: 'POST',
                    headers: headers,
                    body: JSON.stringify({
                        'url': websiteUrl,
                        'number': textInput.value,
                        'name': event.target.textContent
                    })

                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('發生錯誤');
                        }
                        return response.json();
                    })
                    .then(scrapedData => {
                        const content = scrapedData.href.map((href, index) => {
                            const text = scrapedData.text[index];
                            return `${index + 1}: <a href="${href}" target="_blank">${text}</a><br>`;
                        }).join('');
                        newsContent.innerHTML = content;
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
            }
            else {
                if (!newsContent) {
                    alert("還沒進行搜尋!")
                    return;
                }
                else {
                    const apiUrl = '/api/news/save';
                    const innerHTMLHref = newsContent.innerHTML;
                    const innerHTMLText = newsContent.innerText;
                    const hrefArray = [];
                    const textArray = innerHTMLText.split('\n').filter(Boolean);
                    for (let i = 0; i < textArray.length; i++) {
                        if (textArray[i].length >= 2) {
                            textArray[i] = textArray[i].substring(2); // 去除前两个字符
                        }
                    }
                    const regex = /href="([^"]+)"/g;
                    let match;
                    while ((match = regex.exec(innerHTMLHref)) !== null) {
                        const hrefValue = match[1]; // 获取匹配到的href的值
                        hrefArray.push(hrefValue); // 存储到数组中
                    }
                    fetch(port + apiUrl, {
                        method: 'POST',
                        headers: headers,
                        body: JSON.stringify({
                            'href': hrefArray,
                            'text': textArray
                        })
                    })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('發生錯誤');
                            }
                            else {
                                alert("Success!")
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                        });
                }
            }
        }
    });






});
