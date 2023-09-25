// 检查 localStorage 并刷新 <div> 的函数
function checkAndRefreshDiv() {
    //const responseDivCreate = document.getElementById('responseCreate');
    const floatingWindow = document.getElementById('floating-window');
    const jsonData = localStorage.getItem('jsonData'); // 从 localStorage 获取值
    //const publicKeyString = localStorage.getItem('publicKeyString');

    if (jsonData) {
        const token = jsonData.split('.');

        // 刷新 <div> 或执行其他操作
        if (token.length === 3) {
            const encodedPayload = token[1];
            const decodedPayload = atob(encodedPayload);
            const payloadObject = JSON.parse(decodedPayload);

            // 检查JWT令牌中的过期时间（exp）
            const expirationTime = payloadObject.exp;
            const currentTimestamp = Math.floor(Date.now() / 1000); // 当前时间戳（以秒为单位）

            if (expirationTime && currentTimestamp <= expirationTime) {
                // JWT令牌未过期
                floatingWindow.innerHTML = `
                姓名: ${payloadObject.name}<br>
                Identity: ${payloadObject.identity}<br>
                BirthDay: ${payloadObject.birth}<br>
                Phone: ${payloadObject.phone}
                `;
            } else {
                // JWT令牌已过期，清除localStorage中的数据
                localStorage.removeItem('jsonData');
                localStorage.removeItem('publicKeyString');
                floatingWindow.innerHTML = 'Token has expired';
            }
        }
    }
}


