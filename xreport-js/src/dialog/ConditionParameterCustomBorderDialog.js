/**
 * Created by Jacky.Gao on 2017-02-07.
 */

export default class ConditionParameterCustomBorderDialog {
    constructor() {
        this.dialog = $(`<div class="modal fade" role="dialog" aria-hidden="true" style="z-index: 11003">
            <div class="modal-dialog" style="width: 300px">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            自定义边框
                        </h4>
                    </div>
                    <div class="modal-body"></div>
                    <div class="modal-footer"></div>
                </div>
            </div>
        </div>`);
        this.body = this.dialog.find('.modal-body');
        this.footer = this.dialog.find(".modal-footer");
    }


    initBody(body, footer) {
        body.html("");
        const container = $('<div></div>');
        body.append(container);
        const ul = $("<ul class='nav nav-tabs'></ul>");
        container.append(ul);
        const allLi = $("<li class='active'><a data-toggle='tab' href='#_allBorderConfig'>所有</a></li>");
        ul.append(allLi);
        const topLi = $("<li><a data-toggle='tab' href='#_topBorderConfig'>上</a></li>");
        ul.append(topLi);
        const bottomLi = $("<li><a data-toggle='tab' href='#_bottomBorderConfig'>下</a></li>");
        ul.append(bottomLi);
        const leftLi = $("<li><a data-toggle='tab' href='#_leftBorderConfig'>左</a></li>");
        ul.append(leftLi);
        const rightLi = $("<li><a data-toggle='tab' href='#_rightBorderConfig'>右</a></li>");
        ul.append(rightLi);

        const tabContent = $("<div class='tab-content'></div>");

        let buildCellStyle = (direction, active, cellStyles) => {
            let setBorderStyle = function (key, value) {
                if (Array.isArray(cellStyles)) {
                    cellStyles.forEach(bs => bs[key] = value);
                } else {
                    cellStyles[key] = value;
                }
            };

            const borderConfig = $(`<div class="tab-pane fade ${active ? ' in active' : ''}" id="_${direction}BorderConfig"></div>`);
            tabContent.append(borderConfig);

            let borderLineContainer = $(`<div style="margin: 20px 0 10px 0;"><span>${window.i18n.tools.border.lineStyle}：</span></div>`);
            borderConfig.append(borderLineContainer);
            this[`${direction}BorderLineList`] = $(`
                <select class="form-control" style="display: inline-block;width:120px">
                    <option value="solid">${window.i18n.tools.border.solidLine}</option>
                    <option value="dashed">${window.i18n.tools.border.dashed}</option>
                    <option value="none">${window.i18n.tools.border.none}</option>
                </select>
            `);
            borderLineContainer.append(this[`${direction}BorderLineList`]);
            this[`${direction}BorderLineList`].change(function () {
                setBorderStyle('style', $(this).val());
            });

            let borderSizeContainer = $(`<div style="margin: 20px 0 10px 0;"><span>${window.i18n.tools.border.size}：</span></div>`);
            borderConfig.append(borderSizeContainer);
            this[`${direction}BorderSizeList`] = $(`
                <select class="form-control" style="display: inline-block;width:120px">
                </select>
            `);
            for (let i = 1; i <= 10; i++) {
                this[`${direction}BorderSizeList`].append(`<option value="${i}">${i}</option>`);
            }
            borderSizeContainer.append(this[`${direction}BorderSizeList`]);
            this[`${direction}BorderSizeList`].change(function () {
                setBorderStyle('width', $(this).val());
            });

            let borderColor = $(`<div><span>${window.i18n.tools.border.color}：</span></div>`);
            borderConfig.append(borderColor);
            this[`${direction}BorderColorContainer`] = $("<div style=\"display: inline-flex;width: 148px;flex-direction: column;\"> <input type=\"text\" class=\"form-control\"/> </div>");
            borderColor.append(this[`${direction}BorderColorContainer`]);
            this[`${direction}BorderColorContainer`].colorpicker({
                container: true,
                inline: true,
                autoInputFallback: false,
                autoHexInputFallback: false,
                colorSelectors: {
                    'black': '#000000',
                    'red': '#FF0000',
                    'default': '#777777',
                    'primary': '#337ab7',
                    'success': '#5cb85c',
                    'info': '#5bc0de',
                    'warning': '#f0ad4e',
                    'danger': '#d9534f'
                }
            });
            this[`${direction}BorderColorContainer`].colorpicker().on("changeColor", function (e) {
                let rgb = e.color.toRGB();
                let color = rgb.r + "," + rgb.g + "," + rgb.b;
                setBorderStyle('color', color);
            });
        }

        buildCellStyle('all', true, [this.cellStyle.topBorder, this.cellStyle.bottomBorder, this.cellStyle.leftBorder, this.cellStyle.rightBorder])
        buildCellStyle('top', false, this.cellStyle.topBorder);
        buildCellStyle('bottom', false, this.cellStyle.bottomBorder);
        buildCellStyle('left', false, this.cellStyle.leftBorder);
        buildCellStyle('right', false, this.cellStyle.rightBorder);

        container.append(tabContent);
    }

    show(cellStyle) {
        this.cellStyle = cellStyle;

        this.initBody(this.body, this.footer);

        this.dialog.modal('show');

        let directions = ['all', 'top', 'bottom', 'left', 'right'];
        for (let i = 0, len = directions.length; i < len; i++) {
            let direction = i === 0 ? directions[1] : directions[i];
            const border = cellStyle[`${direction}Border`];
            this[`${direction}BorderSizeList`].val(border.width)
            this[`${direction}BorderLineList`].val(border.style);
            this[`${direction}BorderColorContainer`].colorpicker("setValue", "rgb(" + border.color + ")");
        }
    }
}

