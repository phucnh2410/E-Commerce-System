
$(document).ready(function() {

});

async function fetchStatisticData(endpoint){
    const response = await fetch(endpoint);
    return await response.json();
}

function showSellerCharts(sellerId, fullName) {
    // Hiển thị biểu đồ cho seller
    document.getElementById('seller-charts').style.display = 'block';
    const title = document.querySelector('.seller-name-title');
    title.innerText = "Seller: "+ fullName;
    renderSellerCharts(sellerId).then(r => {});
}

function showCustomerCharts(customerId) {
    // Hiển thị biểu đồ cho customer
    document.getElementById('customer-charts').style.display = 'block';
    renderCustomerCharts(customerId).then(r => {});
}

function showCategoryCharts(categoryId) {
    // Hiển thị biểu đồ cho customer
    document.getElementById('category-charts').style.display = 'block';
    renderCategoryCharts(categoryId).then(r => {});
}

let charts = [];

function createChart(ctx, labels, revenueData, quantityData, title, chartId) {
    if (charts[chartId]){
        charts[chartId].destroy();
    }

    charts[chartId] = new Chart(ctx, {
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


async function renderSellerCharts(sellerId) {
    const monthlySellerData = await fetchStatisticData('/api/statistic/seller/monthly/'+sellerId);
    const quarterlySellerData = await fetchStatisticData('/api/statistic/seller/quarterly/'+sellerId);
    const yearlySellerData = await fetchStatisticData('/api/statistic/seller/yearly/'+sellerId);

    // Monthly Chart
    // const currentMonths = monthlyData.months.map(month => `${month} ${currentYear}`);
    const ctxMonthlySeller = document.getElementById('barMonthlySellerChart').getContext('2d');
    createChart(ctxMonthlySeller, monthlySellerData.months, monthlySellerData.revenue, monthlySellerData.quantity, 'Monthly Sales Data', 'barMonthlySellerChart');

    // Quarterly Chart
    const ctxQuarterlySeller = document.getElementById('barQuarterlySellerChart').getContext('2d');
    createChart(ctxQuarterlySeller, quarterlySellerData.quarters, quarterlySellerData.revenue, quarterlySellerData.quantity, 'Quarterly Sales Data', 'barQuarterlySellerChart');

    // Yearly Chart
    const ctxYearlySeller = document.getElementById('barYearlySellerChart').getContext('2d');
    createChart(ctxYearlySeller, yearlySellerData.years, yearlySellerData.revenue, yearlySellerData.quantity, 'Yearly Sales Data', 'barYearlySellerChart');

}

async function renderCustomerCharts(customerId) {
////////////////////////////////////////////////* Customer *//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    const monthlyCustomerData = await fetchStatisticData('/api/statistic/customer/monthly/'+customerId);
    const quarterlyCustomerData = await fetchStatisticData('/api/statistic/customer/quarterly/'+customerId);
    const yearlyCustomerData = await fetchStatisticData('/api/statistic/customer/yearly/'+customerId);

    // Monthly Chart
    // const currentMonths = monthlyData.months.map(month => `${month} ${currentYear}`);
    const ctxMonthlyCustomer = document.getElementById('barMonthlyCustomerChart').getContext('2d');
    createChart(ctxMonthlyCustomer, monthlyCustomerData.months, monthlyCustomerData.totalAmount, monthlyCustomerData.quantity, 'Monthly Customer Data', 'barMonthlyCustomerChart');

    // Quarterly Chart
    const ctxQuarterlyCustomer = document.getElementById('barQuarterlyCustomerChart').getContext('2d');
    createChart(ctxQuarterlyCustomer, quarterlyCustomerData.quarters, quarterlyCustomerData.totalAmount, quarterlyCustomerData.quantity, 'Quarterly Customer Data', 'barQuarterlyCustomerChart');
    //
    // Yearly Chart
    const ctxYearlyCustomer = document.getElementById('barYearlyCustomerChart').getContext('2d');
    createChart(ctxYearlyCustomer, yearlyCustomerData.years, yearlyCustomerData.totalAmount, yearlyCustomerData.quantity, 'Yearly Customer Data', 'barYearlyCustomerChart');
}

async function renderCategoryCharts(categoryId) {
////////////////////////////////////////////////* Customer *//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    const monthlyCategoryData = await fetchStatisticData('/api/statistic/category/monthly/'+categoryId);
    const quarterlyCategoryData = await fetchStatisticData('/api/statistic/category/quarterly/'+categoryId);
    const yearlyCategoryData = await fetchStatisticData('/api/statistic/category/yearly/'+categoryId);

    // Monthly Chart
    // const currentMonths = monthlyData.months.map(month => `${month} ${currentYear}`);
    const ctxMonthlyCategory = document.getElementById('barMonthlyCategoryChart').getContext('2d');
    createChart(ctxMonthlyCategory, monthlyCategoryData.months, monthlyCategoryData.revenue, monthlyCategoryData.quantity, 'Monthly Category Data', 'barMonthlyCategoryChart');

    // Quarterly Chart
    const ctxQuarterlyCategory = document.getElementById('barQuarterlyCategoryChart').getContext('2d');
    createChart(ctxQuarterlyCategory, quarterlyCategoryData.quarters, quarterlyCategoryData.revenue, quarterlyCategoryData.quantity, 'Quarterly Category Data', 'barQuarterlyCategoryChart');
    //
    // Yearly Chart
    const ctxYearlyCategory = document.getElementById('barYearlyCategoryChart').getContext('2d');
    createChart(ctxYearlyCategory, yearlyCategoryData.years, yearlyCategoryData.revenue, yearlyCategoryData.quantity, 'Yearly Category Data', 'barYearlyCategoryChart');
}