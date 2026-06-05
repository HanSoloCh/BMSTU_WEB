async function loadStatus() {
    const statusDiv = document.getElementById('status-data');
    const lastUpdateDiv = document.getElementById('last-update');
    
    try {
        statusDiv.innerHTML = '<div class="loading">Загрузка данных...</div>';
        
        const response = await fetch('/nginx_status');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const text = await response.text();
        const statusData = parseNginxStatus(text);
        displayStatus(statusData);
        
        if (lastUpdateDiv) {
            lastUpdateDiv.textContent = 'Последнее обновление: ' + new Date().toLocaleTimeString('ru-RU');
        }
    } catch (error) {
        statusDiv.innerHTML = `
            <div class="error">
                <strong>Ошибка загрузки данных:</strong><br>
                ${error.message}
            </div>
        `;
    }
}

function parseNginxStatus(text) {
    const lines = text.trim().split('\n');
    const data = {};
    
    lines.forEach(line => {
        if (line.includes('Active connections:')) {
            data.activeConnections = line.match(/\d+/)[0];
        } else if (line.includes('server accepts handled requests')) {
            const numbers = line.match(/\d+/g);
            if (numbers) {
                data.accepts = numbers[0];
                data.handled = numbers[1];
                data.requests = numbers[2];
            }
        } else if (line.match(/^\s+\d+\s+\d+\s+\d+$/)) {
            const numbers = line.trim().split(/\s+/);
            if (numbers.length >= 3) {
                data.accepts = numbers[0];
                data.handled = numbers[1];
                data.requests = numbers[2];
            }
        } else if (line.includes('Reading:')) {
            const reading = line.match(/Reading:\s*(\d+)/);
            const writing = line.match(/Writing:\s*(\d+)/);
            const waiting = line.match(/Waiting:\s*(\d+)/);
            
            if (reading) data.reading = reading[1];
            if (writing) data.writing = writing[1];
            if (waiting) data.waiting = waiting[1];
        }
    });
    
    return data;
}

function displayStatus(data) {
    const statusDiv = document.getElementById('status-data');
    
    const acceptRate = data.accepts && data.handled ? 
        ((data.handled / data.accepts) * 100).toFixed(2) : 'N/A';
    
    const requestsPerConnection = data.requests && data.handled ? 
        (data.requests / data.handled).toFixed(2) : 'N/A';
    
    statusDiv.innerHTML = `
        <table class="status-table">
            <tr>
                <th>Параметр</th>
                <th>Значение</th>
            </tr>
            <tr>
                <td>Активные подключения</td>
                <td class="status-value">${data.activeConnections || 'N/A'}</td>
            </tr>
            <tr>
                <td>Принято подключений</td>
                <td>${data.accepts || 'N/A'}</td>
            </tr>
            <tr>
                <td>Обработано подключений</td>
                <td>${data.handled || 'N/A'}</td>
            </tr>
            <tr>
                <td>Всего запросов</td>
                <td class="status-value">${data.requests || 'N/A'}</td>
            </tr>
            <tr>
                <td>Чтение заголовков</td>
                <td>${data.reading || 'N/A'}</td>
            </tr>
            <tr>
                <td>Запись ответов</td>
                <td>${data.writing || 'N/A'}</td>
            </tr>
            <tr>
                <td>Ожидание Keep-Alive</td>
                <td>${data.waiting || 'N/A'}</td>
            </tr>
            <tr>
                <td>Процент успешных подключений</td>
                <td>${acceptRate}%</td>
            </tr>
            <tr>
                <td>Запросов на подключение</td>
                <td>${requestsPerConnection}</td>
            </tr>
        </table>
    `;
}

let autoRefreshInterval = null;
let autoRefreshEnabled = true;

function startAutoRefresh() {
    if (autoRefreshInterval) {
        clearInterval(autoRefreshInterval);
    }
    if (autoRefreshEnabled) {
        autoRefreshInterval = setInterval(loadStatus, 5000);
        updateAutoRefreshButton();
    }
}

function stopAutoRefresh() {
    if (autoRefreshInterval) {
        clearInterval(autoRefreshInterval);
        autoRefreshInterval = null;
    }
    updateAutoRefreshButton();
}

function toggleAutoRefresh() {
    autoRefreshEnabled = !autoRefreshEnabled;
    if (autoRefreshEnabled) {
        startAutoRefresh();
    } else {
        stopAutoRefresh();
    }
}

function updateAutoRefreshButton() {
    const btn = document.getElementById('auto-refresh-btn');
    if (btn) {
        if (autoRefreshEnabled && autoRefreshInterval) {
            btn.textContent = 'Остановить автообновление';
        } else {
            btn.textContent = 'Возобновить автообновление';
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    loadStatus();
    startAutoRefresh();
    window.addEventListener('beforeunload', stopAutoRefresh);
});
