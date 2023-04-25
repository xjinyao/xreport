/**
 * Created by Jacky.Gao on 2017-02-12.
 */
import {formatDate, resetDirty} from '../Utils.js';
import {alert, confirm} from '../MsgBox.js';

export default class SaveDialog {
    constructor() {
        this.isUpdate = false;
        this.reportFilesData = {};
        this.dialog = $(`<div class="modal fade" role="dialog" aria-hidden="true" style="z-index: 10000;max-height: 100vh;overflow: auto">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            ${window.i18n.dialog.save.title}
                        </h4>
                    </div>
                    <div class="modal-body"></div>
                    <div class="modal-footer"></div>
                </div>
            </div>
        </div>`);
        const body = this.dialog.find('.modal-body'), footer = this.dialog.find(".modal-footer");
        this.initBody(body);
        this.initFooter(footer);
    }

    initBody(body) {
        const reportTypeGroup = $(`<div class="form-group input-group"><label class="input-group-addon">${window.i18n.dialog.save.reportType}:</label></div>`);
        this.reportTypeSelect = $(`<select class="form-control">`);
        reportTypeGroup.append(this.reportTypeSelect);
        body.append(reportTypeGroup);

        const reportNameGroup = $(`<div class="form-group input-group"><label class="input-group-addon">${window.i18n.dialog.save.reportName}:</label></div>`);
        this.reportNameEditor = $(`<input type="text" class="form-control">`);
        reportNameGroup.append(this.reportNameEditor);
        body.append(reportNameGroup);

        const fileGroup = $(`<div class="form-group input-group"><label class="input-group-addon">${window.i18n.dialog.save.fileName}:</label></div>`);
        this.fileEditor = $(`<input type="text" class="form-control" ${this.isUpdate ? "disabled" : ""}>`);
        fileGroup.append(this.fileEditor);
        body.append(fileGroup);

        const isTemplateGroup = $(`<div class="form-group input-group"><label class="input-group-addon">${window.i18n.dialog.save.isTemplate}:</label></div>`);
        this.isTemplateEditor = $(`<select class="form-control" ${this.isUpdate ? "disabled" : ""}>`);
        this.isTemplateEditor.append(`<option value="true">${window.i18n.dialog.save.params.dict.yes}</option>`);
        this.isTemplateEditor.append(`<option value="false">${window.i18n.dialog.save.params.dict.no}</option>`);
        isTemplateGroup.append(this.isTemplateEditor);
        body.append(isTemplateGroup);

        const visibleGroup = $(`<div class="form-group input-group"><label class="input-group-addon">${window.i18n.dialog.save.visible}:</label></div>`);
        this.visibleEditor = $(`<select class="form-control">`);
        this.visibleEditor.append(`<option value="true">${window.i18n.dialog.save.params.dict.yes}</option>`);
        this.visibleEditor.append(`<option value="false">${window.i18n.dialog.save.params.dict.no}</option>`);
        visibleGroup.append(this.visibleEditor);
        body.append(visibleGroup);

        const previewImmediatelyLoadGroup = $(`<div class="form-group input-group"><label class="input-group-addon">${window.i18n.dialog.save.previewImmediatelyLoad}:</label></div>`);
        this.previewImmediatelyLoadEditor = $(`<select class="form-control">`);
        this.previewImmediatelyLoadEditor.append(`<option value="true">${window.i18n.dialog.save.params.dict.yes}</option>`);
        this.previewImmediatelyLoadEditor.append(`<option value="false">${window.i18n.dialog.save.params.dict.no}</option>`);
        previewImmediatelyLoadGroup.append(this.previewImmediatelyLoadEditor);
        body.append(previewImmediatelyLoadGroup);

        const descriptionGroup = $(`<div class="form-group input-group"><label class="input-group-addon">${window.i18n.dialog.save.description}:</label></div>`);
        this.descriptionEditor = $(`<textarea class="form-control"></textarea>`);
        descriptionGroup.append(this.descriptionEditor);
        body.append(descriptionGroup);

        const previewParamsDeclarationConfigGroup = $(`<div class="form-group input-group"><label class="input-group-addon">${window.i18n.dialog.save.previewParamsDeclarationConfig}:</label></div>`);
        this.previewParamsDeclarationConfigTable = $(`<table class="table table-bordered" style="margin-bottom: 0;">
                                                        <thead>
                                                            <tr style="background: #f4f4f4;height: 30px;">
                                                                <td style="vertical-align: middle">${window.i18n.dialog.save.params.name}</td>
                                                                <td style="width: 150px;vertical-align: middle">${window.i18n.dialog.save.params.code}</td>
                                                                <td style="width:100px;vertical-align: middle">${window.i18n.dialog.save.params.type}</td>
                                                                <td style="width:100px;vertical-align: middle">${window.i18n.dialog.save.params.required}</td>
                                                                <td style="width:100px;vertical-align: middle">${window.i18n.dialog.save.params.defaultValue}</td>
                                                                <td style="vertical-align: middle">${window.i18n.dialog.save.params.description}</td>
                                                                <td style="width:35px;padding:0;vertical-align: middle"><button type="button" id="addParam" class="btn btn-primary"><i class="icon-plus"></i> 新增 </button></td>
                                                            </tr>
                                                        </thead>
                                                    </table>`);
        this.previewParamsDeclarationConfigTableBody = $(`<tbody></tbody>`);
        this.previewParamsDeclarationConfigTable.append(this.previewParamsDeclarationConfigTableBody);
        previewParamsDeclarationConfigGroup.append(this.previewParamsDeclarationConfigTable);
        body.append(previewParamsDeclarationConfigGroup);

        const providerGroup = $(`<div class="form-group input-group"><label class="input-group-addon">${window.i18n.dialog.save.source}:</label></div>`);
        this.providerSelect = $(`<select class="form-control" ${this.isUpdate ? "disabled" : ""}>`);
        providerGroup.append(this.providerSelect);
        body.append(providerGroup);

        const tableContainer = $(`<div style="height:350px;overflow: auto"></div>`);
        body.append(tableContainer);

        const fileTable = $(`<table class="table table-bordered"><thead><tr style="background: #f4f4f4;height: 30px;"><td style="vertical-align: middle">${window.i18n.dialog.save.fileName}</td><td style="width: 150px;vertical-align: middle">${window.i18n.dialog.save.modDate}</td><td style="width:50px;vertical-align: middle">${window.i18n.dialog.save.del}</td></tr></thead></table>`);
        this.fileTableBody = $(`<tbody></tbody>`);
        fileTable.append(this.fileTableBody);
        tableContainer.append(fileTable);

        const _this = this;
        this.previewParamsDeclarationConfigTable.find("#addParam").click(function () {
            _this.previewParamsDeclarationConfigTableBody.append(`
            <tr>
                <td style="padding: 0px"><input style="border: none" type="text" class="form-control"/></td>
                <td style="padding: 0px"><input style="border: none" type="text" class="form-control"/></td>
                <td style="padding: 0px">
                    <select style="border: none"  class="form-control">
                        <option value="String">${window.i18n.dialog.save.params.dict.string}</option>
                        <option value="Number">${window.i18n.dialog.save.params.dict.number}</option>
                        <option value="Boolean">${window.i18n.dialog.save.params.dict.boolean}</option>
                    </select>
                </td>
                <td style="padding: 0px">
                    <select style="border: none"  class="form-control">
                        <option value="true">${window.i18n.dialog.save.params.dict.yes}</option>
                        <option value="false">${window.i18n.dialog.save.params.dict.no}</option>
                    </select>
                </td style="padding: 0px">
                <td style="padding: 0px"><input style="border: none"  type="text" class="form-control"/></td>
                <td style="padding: 0px"><input style="border: none"  type="text" class="form-control"/></td>
                <td style="text-align: center;padding-top: 5px;vertical-align: middle;">
                     <a href="javascript:" class="removeTr"><i class="glyphicon glyphicon-trash" style="color: red;font-size: 14pt"></i></a>
                </td>
            </tr>
            `);
        });
        this.previewParamsDeclarationConfigTableBody.on("click", ".removeTr", function () {
            $(this).parents("tr").remove();
        });


        this.providerSelect.change(function () {
            let value = $(this).val();
            if (!value || value === '') {
                return;
            }
            _this.fileTableBody.empty();
            let reportFiles = _this.reportFilesData[value];
            if (!reportFiles) {
                return;
            }
            for (let file of reportFiles) {
                let tr = $(`<tr style="height: 35px;"></tr>`);
                _this.fileTableBody.append(tr);
                tr.append(`<td style="vertical-align: middle">${file.name}</td>`);
                tr.append(`<td style="vertical-align: middle">${formatDate(file.updateDate)}</td>`);
                let deleteCol = $(`<td style="text-align: center;padding-top: 5px;vertical-align: middle;"></td>`);
                tr.append(deleteCol);
                let deleteIcon = $(`<a href="###"><i class="glyphicon glyphicon-trash" style="color: red;font-size: 14pt"></i></a>`);
                deleteCol.append(deleteIcon);
                deleteIcon.click(function () {
                    confirm(`${window.i18n.dialog.save.delConfirm}` + file.name, function () {
                        let fullFile = value + file.name;
                        $.ajax({
                            type: 'POST',
                            data: {file: fullFile},
                            url: window._server + "/designer/deleteReportFile",
                            success: function () {
                                tr.remove();
                                let index = reportFiles.indexOf(file);
                                reportFiles.splice(index, 1);
                            },
                            error: function (response) {
                                if (response && response.responseText) {
                                    alert("服务端错误：" + response.responseText + "");
                                } else {
                                    alert(`${window.i18n.dialog.save.delFail}`);
                                }
                            }
                        });
                    });
                });
            }
            _this.currentProviderPrefix = value;
            _this.currentReportFiles = reportFiles;
        });
    }

    initFooter(footer) {
        const saveButton = $(`<button type="button" class="btn btn-primary">${window.i18n.dialog.save.save}</button>`);
        footer.append(saveButton);
        const _this = this;
        saveButton.click(function () {
            let reportType = _this.reportTypeSelect.val();
            if (reportType === '') {
                alert(`${window.i18n.dialog.save.reportTypeTip}`);
                return;
            }
            let reportName = _this.reportNameEditor.val();
            if (reportName === '') {
                alert(`${window.i18n.dialog.save.reportNameTip}`);
                return;
            }
            let fileName = _this.fileEditor.val();
            if (fileName === '') {
                alert(`${window.i18n.dialog.save.nameTip}`);
                return;
            }
            let isTemplate = _this.isTemplateEditor.val();
            let visible = _this.visibleEditor.val();
            let previewImmediatelyLoad = _this.previewImmediatelyLoadEditor.val();
            let description = _this.descriptionEditor.val();
            if (description === '') {
                alert(`${window.i18n.dialog.save.descriptionTip}`);
                return;
            }
            let trs = _this.previewParamsDeclarationConfigTable.find("tbody tr");
            let valuesArr = [];
            for (let i = 0, len = trs.length; i < len; i++) {
                let tds = trs.eq(i).find("td");
                let values = [];
                for (let j = 0, len_j = tds.length; j < len_j; j++) {
                    let target = tds.eq(j).find("input,select");
                    let val = target.val();
                    if (j === 0 && /^\s*$/.test(val)) {
                        alert("请填写参数名称");
                        target.focus();
                        return;
                    }
                    if (j === 1 && /^\s*$/.test(val)) {
                        alert("请填写参数编码");
                        target.focus();
                        return;
                    }
                    values.push(val);
                }
                let item = {
                    name: values[0],
                    code: values[1],
                    type: values[2],
                    required: values[3],
                    defaultValue: values[4],
                    description: values[5],
                };
                valuesArr.push(item);
            }

            let previewParamsDeclarationConfig = JSON.stringify(valuesArr);
            if (!_this.currentProviderPrefix || !_this.currentReportFiles) {
                alert(`${window.i18n.dialog.save.locationTip}`);
                return;
            }
            for (let file of _this.currentReportFiles) {
                let fname = file.name;
                let pos = fname.indexOf(".");
                fname = fname.substring(0, pos);
                if (fname === fileName) {
                    alert(`${window.i18n.dialog.save.file}[${fileName}]${window.i18n.dialog.save.exist}`);
                    return;
                }
            }
            if (!_this.isUpdate) {
                fileName = _this.currentProviderPrefix + fileName + ".xreport.xml";
            } else {
                fileName = _this.currentProviderPrefix + fileName;
            }
            $.ajax({
                url: window._server + "/designer/saveReportFile",
                data: {
                    reportType: reportType,
                    reportName: reportName,
                    file: fileName,
                    description: description,
                    content: _this.content,
                    isTemplate: isTemplate,
                    visible: visible,
                    previewImmediatelyLoad: previewImmediatelyLoad,
                    previewParamsDeclarationConfig: previewParamsDeclarationConfig,
                },
                type: 'POST',
                success: function () {
                    alert(`${window.i18n.dialog.save.success}`);
                    window._reportFile = fileName;
                    _this.context.fileInfo.setFile(fileName);
                    resetDirty();
                    _this.dialog.modal('hide');
                },
                error: function (response) {
                    if (response && response.responseText) {
                        alert("服务端错误：" + response.responseText + "");
                    } else {
                        alert(`${window.i18n.dialog.save.fail}`);
                    }
                }
            });
        });
    }

    show(content, context, isUpdate) {
        this.isUpdate = isUpdate === undefined ? false : isUpdate;
        this.content = content;
        this.context = context;
        const _this = this;
        if (isUpdate) {
            $.ajax({
                url: window._server + '/designer/loadReportInfo',
                data: {file: window._reportFile},
                success: function (res) {
                    console.log("==>", res)
                    _this.reportNameEditor.val(res.name);
                    _this.isTemplateEditor.val(`${res.isTemplate}`);
                    _this.visibleEditor.val(`${res.visible}`);
                    _this.previewImmediatelyLoadEditor.val(`${res.previewImmediatelyLoad}`);
                    _this.descriptionEditor.val(res.description);
                    _this.fileEditor.val(res.fileName);
                    _this.reportTypeSelect.val(`${res.type}`);
                    _this.providerSelect.empty();
                    _this.fileTableBody.empty();
                    _this.reportFilesData = {};
                    let previewParamsDeclarationConfig = res.previewParamsDeclarationConfig || [];
                    let configs = JSON.parse(previewParamsDeclarationConfig);
                    let aa = 1;
                    for (let i = 0, len = configs.length; i < len; i++) {
                        let config = configs[i];
                        let tr = $(`<tr></tr>`);
                        tr.append(`<td style="padding: 0px"><input style="border: none" type="text" class="form-control" value="${config.name}"/></td>`);
                        tr.append(`<td style="padding: 0px"><input style="border: none" type="text" class="form-control" value="${config.code}"/></td>`);

                        let paramTypeSelect = $(`<select style="border: none"  class="form-control">
                                                    <option value="String">${window.i18n.dialog.save.params.dict.string}</option>
                                                    <option value="Number">${window.i18n.dialog.save.params.dict.number}</option>
                                                    <option value="Boolean">${window.i18n.dialog.save.params.dict.boolean}</option>
                                                </select>`);
                        let paramTypeTd = $(`<td style="padding: 0px"></td>`);
                        paramTypeTd.append(paramTypeSelect);
                        tr.append(paramTypeTd);


                        let paramRequiredSelect = $(`<select style="border: none"  class="form-control">
                                                        <option value="true">${window.i18n.dialog.save.params.dict.yes}</option>
                                                        <option value="false">${window.i18n.dialog.save.params.dict.no}</option>
                                                    </select>`);
                        let paramRequiredTd = $(`<td style="padding: 0px"></td>`)
                        paramRequiredTd.append(paramRequiredSelect);

                        tr.append(paramRequiredTd);
                        tr.append(`<td style="padding: 0px"><input style="border: none" type="text" class="form-control" value="${config.defaultValue}"/></td>`);
                        tr.append(`<td style="padding: 0px"><input style="border: none" type="text" class="form-control" value="${config.description}"/></td>`);
                        tr.append(`<td style="text-align: center;padding-top: 5px;vertical-align: middle;">
                                     <a href="javascript:" class="removeTr"><i class="glyphicon glyphicon-trash" style="color: red;font-size: 14pt"></i></a>
                                   </td>`);


                        _this.previewParamsDeclarationConfigTableBody.append(tr);


                        paramTypeSelect.val(`${config.type}`);
                        paramRequiredSelect.val(`${config.required}`);
                    }
                },
                error: function (response) {
                    if (response && response.responseText) {
                        alert("服务端错误：" + response.responseText + "");
                    } else {
                        alert(`${window.i18n.dialog.save.loadFail}`);
                    }
                }
            });
        } else {
            this.reportNameEditor.val('');
            this.isTemplateEditor.val("false");
            this.visibleEditor.val("true");
            this.previewImmediatelyLoadEditor.val("true");
            this.descriptionEditor.val('');
            this.fileEditor.val('');
            this.reportTypeSelect.empty();
            this.providerSelect.empty();
            this.previewParamsDeclarationConfigTableBody.empty();
            this.fileTableBody.empty();
            this.reportFilesData = {};
        }
        $.ajax({
            url: window._server + '/designer/loadReportProviders',
            success: function (providers) {
                for (let provider of providers) {
                    let {reportFiles, name, prefix} = provider;
                    _this.reportFilesData[prefix] = reportFiles;
                    _this.providerSelect.append(`<option value="${prefix}">${name}</option>`);
                }
                _this.providerSelect.trigger('change');
            },
            error: function (response) {
                if (response && response.responseText) {
                    alert("服务端错误：" + response.responseText + "");
                } else {
                    alert(`${window.i18n.dialog.save.loadFail}`);
                }
            }
        });
        $.ajax({
            url: window._server + '/dict/loadReportDict?type=report_type',
            success: function (dicts) {
                _this.reportTypeData = dicts;
                for (let dict of dicts) {
                    let {label, value} = dict;
                    _this.reportTypeSelect.append(`<option value="${value}">${label}</option>`);
                }
                _this.reportTypeSelect.trigger('change');
            },
            error: function (response) {
                if (response && response.responseText) {
                    alert("服务端错误：" + response.responseText + "");
                } else {
                    alert(`${window.i18n.dialog.save.loadFail}`);
                }
            }
        });
        this.dialog.modal('show');
    }
}