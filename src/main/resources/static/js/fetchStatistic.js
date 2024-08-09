// import ChartDataLabels from 'chartjs-plugin-datalabels';
// import Chart from 'chart.js/auto'; // Đảm bảo rằng bạn đã nhập Chart.js
// import ChartDataLabels from 'chartjs-plugin-datalabels';

$(document).ready(function() {
    renderCharts().then(r => {});
});

async function fetchStatisticData(endpoint){
    const response = await fetch(endpoint);
    return await response.json();
}

function createChart(ctx, labels, revenueData, quantityData, title) {
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Revenue',
                    data: revenueData,
                    backgroundColor: 'rgba(0,74,154,0.2)',
                    borderColor: 'rgb(4,79,189)',
                    borderWidth: 1
                },
                {
                    label: 'Quantity',
                    data: quantityData,
                    backgroundColor: 'rgba(19,131,0,0.2)',
                    borderColor: 'rgb(19,192,9)',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.dataset.label + ': ' + tooltipItem.raw;
                        }
                    }
                },
                // Thêm cấu hình tiêu đề
                title: {
                    display: true, // Hiển thị tiêu đề
                    text: title,   // Tiêu đề của biểu đồ
                    font: {
                        size: 16,   // Kích thước chữ tiêu đề
                        weight: 'bold' // Độ đậm của chữ tiêu đề
                    }
                },
                datalabels: {
                    display: true,
                    color: '#000', // Màu chữ của nhãn dữ liệu
                    anchor: 'end', // Vị trí của nhãn dữ liệu
                    align: 'top', // Căn chỉnh nhãn dữ liệu
                    formatter: function(value, context) {
                        // Kiểm tra nếu label là 'Revenue' để thêm dấu $
                        if (context.dataset.label === 'Revenue') {
                            return '$' + value.toFixed(1);
                        }
                        if (context.dataset.label === 'Quantity') {
                            return 'x' + value;
                        }
                        return value;
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        },
        plugins: [ChartDataLabels]
    });
}


async function renderCharts() {
    const monthlySellerData = await fetchStatisticData('/api/statistic/seller/monthly/6');
    const quarterlySellerData = await fetchStatisticData('/api/statistic/seller/quarterly/6');
    const yearlySellerData = await fetchStatisticData('/api/statistic/seller/yearly/6');

    // Monthly Chart
    // const currentMonths = monthlyData.months.map(month => `${month} ${currentYear}`);
    const ctxMonthlySeller = document.getElementById('barMonthlySellerChart').getContext('2d');
    createChart(ctxMonthlySeller, monthlySellerData.months, monthlySellerData.revenue, monthlySellerData.quantity, 'Monthly Sales Data ');

    // Quarterly Chart
    const ctxQuarterlySeller = document.getElementById('barQuarterlySellerChart').getContext('2d');
    createChart(ctxQuarterlySeller, quarterlySellerData.quarters, quarterlySellerData.revenue, quarterlySellerData.quantity, 'Quarterly Sales Data ');

    // Yearly Chart
    const ctxYearlySeller = document.getElementById('barYearlySellerChart').getContext('2d');
    createChart(ctxYearlySeller, yearlySellerData.years, yearlySellerData.revenue, yearlySellerData.quantity, 'Yearly Sales Data');



////////////////////////////////////////////////* Customer *//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    const monthlyCustomerData = await fetchStatisticData('/api/statistic/customer/monthly/7');
    // const quarterlyCustomerData = await fetchStatisticData('/api/statistic/customer/quarterly/7');
    // const yearlyCustomerData = await fetchStatisticData('/api/statistic/customer/yearly/7');

    // Monthly Chart
    // const currentMonths = monthlyData.months.map(month => `${month} ${currentYear}`);
    const ctxMonthlyCustomer = document.getElementById('barMonthlyCustomerChart').getContext('2d');
    createChart(ctxMonthlyCustomer, monthlyCustomerData.months, monthlyCustomerData.totalAmount, monthlyCustomerData.quantity, 'Monthly Customer Data ');

    // // Quarterly Chart
    // const ctxQuarterlyCustomer = document.getElementById('barQuarterlyCustomerChart').getContext('2d');
    // createChart(ctxQuarterlyCustomer, quarterlyCustomerData.quarters, quarterlyCustomerData.revenue, quarterlyCustomerData.quantity, 'Quarterly Sales Data ');
    //
    // // Yearly Chart
    // const ctxYearlyCustomer = document.getElementById('barYearlyCustomerChart').getContext('2d');
    // createChart(ctxYearlyCustomer, yearlyCustomerData.years, yearlyCustomerData.revenue, yearlyCustomerData.quantity, 'Yearly Sales Data');
}