// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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

function setEnabled(checkBox) {
    let id = $(checkBox).parent().parent().attr('id');

    $.ajax({
        type: "POST",
        url: context.ajaxUrl + 'setEnabled',
        data: {id: id, enable: checkBox.checked}
    }).done(function () {
        updateTable();
        successNoty("Saved");
    });
}