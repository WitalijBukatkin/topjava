let filterForm = $('#filterForm');
var filterData;

$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        }
    );
});

function filter() {
    if (filterData == null) {
        filterData = filterForm.serialize();
    }

    $.ajax({
        type: "GET",
        url: context.ajaxUrl + 'filter',
        data: filterData
    }).done(function (data) {
        $("#filter").modal("hide");
        context.datatableApi.clear().rows.add(data).draw();
        successNoty("Set filter");
    });
}

function resetFilter() {
    filterData = null;
    updateTable();
}

function updateTable() {
    if (filterData !== null) {
        filter();
        return
    }

    $.get(context.ajaxUrl, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}