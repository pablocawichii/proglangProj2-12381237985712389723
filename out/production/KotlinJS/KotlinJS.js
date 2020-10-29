if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'KotlinJS'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'KotlinJS'.");
}var KotlinJS = function (_, Kotlin) {
  'use strict';
  var throwCCE = Kotlin.throwCCE;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var lastOrNull = Kotlin.kotlin.collections.lastOrNull_2p1efm$;
  var toChar = Kotlin.toChar;
  var matches = Kotlin.kotlin.text.matches_rjktp$;
  var replace = Kotlin.kotlin.text.replace_680rmw$;
  var replaceFirst = Kotlin.kotlin.text.replaceFirst_680rmw$;
  var contains = Kotlin.kotlin.text.contains_sgbm27$;
  var Regex_init = Kotlin.kotlin.text.Regex_init_61zpoe$;
  var toDouble = Kotlin.kotlin.text.toDouble_pdl1vz$;
  var toInt = Kotlin.kotlin.text.toInt_pdl1vz$;
  var numberToInt = Kotlin.numberToInt;
  var toByte = Kotlin.toByte;
  var removeLast = Kotlin.kotlin.collections.removeLast_vvxzk3$;
  var asReversed = Kotlin.kotlin.collections.asReversed_vvxzk3$;
  var ensureNotNull = Kotlin.ensureNotNull;
  var last = Kotlin.kotlin.collections.last_2p1efm$;
  var Unit = Kotlin.kotlin.Unit;
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  var copyToArray = Kotlin.kotlin.collections.copyToArray;
  var ArrayList_init_0 = Kotlin.kotlin.collections.ArrayList_init_ww73n8$;
  var Array_0 = Array;
  CanvasTextItem.prototype = Object.create(CanvasItem.prototype);
  CanvasTextItem.prototype.constructor = CanvasTextItem;
  CanvasBarItem.prototype = Object.create(CanvasItem.prototype);
  CanvasBarItem.prototype.constructor = CanvasBarItem;
  CanvasEdgeItem.prototype = Object.create(CanvasItem.prototype);
  CanvasEdgeItem.prototype.constructor = CanvasEdgeItem;
  CanvasFillItem.prototype = Object.create(CanvasItem.prototype);
  CanvasFillItem.prototype.constructor = CanvasFillItem;
  var inp;
  var button;
  var canvas;
  var modalCont;
  var modal;
  var modalDiv;
  var tokens;
  var flowState;
  var derivationError;
  var parsedItems;
  var canvasState;
  function get_context() {
    var tmp$;
    return Kotlin.isType(tmp$ = canvas.getContext('2d'), CanvasRenderingContext2D) ? tmp$ : throwCCE();
  }
  function Pos(x, y) {
    this.x = x;
    this.y = y;
  }
  Pos.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Pos',
    interfaces: []
  };
  function CanvasState(canvas) {
    this.canvas = canvas;
    this.width = this.canvas.width;
    this.height = this.canvas.height;
    this.context = get_context();
    this.xMax = 9;
    this.yMax = 9;
    this.graph = false;
    this.items = ArrayList_init();
  }
  CanvasState.prototype.setAxis_vux9f0$ = function (x, y) {
    this.xMax = x;
    this.yMax = y;
  };
  CanvasState.prototype.addItem_cgrhq8$ = function (item) {
    this.items.add_11rb$(item);
  };
  CanvasState.prototype.addItems_10kp50$ = function (newItems) {
    var tmp$;
    tmp$ = newItems.iterator();
    while (tmp$.hasNext()) {
      var i = tmp$.next();
      this.items.add_11rb$(i);
    }
  };
  CanvasState.prototype.emptyItems = function () {
    while (lastOrNull(this.items) != null) {
      this.items.removeAt_za3lpa$(0);
    }
    this.clear();
  };
  CanvasState.prototype.isGraph = function () {
    this.graph = true;
  };
  CanvasState.prototype.notGraph = function () {
    this.graph = false;
  };
  CanvasState.prototype.drawGraph = function () {
    var tmp$, tmp$_0;
    this.context.strokeStyle = '#000000';
    this.context.lineWidth = 1.0;
    this.context.strokeText('Y-Axis', 35.0, 35.0);
    this.context.fillStyle = '#000000';
    this.context.fillRect(50.0, 50.0, 1.0, 450.0);
    this.context.fillStyle = '#000000';
    this.context.fillRect(500.0, 50.0, 1.0, 450.0);
    tmp$ = this.yMax + 1 | 0;
    for (var i = 0; i < tmp$; i++) {
      this.context.strokeStyle = '#000000';
      this.context.lineWidth = 1.0;
      this.context.strokeText(i.toString(), 25.0, 500.0 - 450.0 / this.yMax * i);
    }
    this.context.strokeStyle = '#000000';
    this.context.lineWidth = 1.0;
    this.context.strokeText('X-Axis', 515.0, 500.0);
    this.context.fillStyle = '#000000';
    this.context.fillRect(50.0, 500.0, 450.0, 1.0);
    this.context.fillStyle = '#000000';
    this.context.fillRect(50.0, 50.0, 450.0, 1.0);
    tmp$_0 = this.xMax + 1 | 0;
    for (var i_0 = 0; i_0 < tmp$_0; i_0++) {
      this.context.strokeStyle = '#000000';
      this.context.lineWidth = 1.0;
      this.context.strokeText(String.fromCharCode(toChar(97 + i_0 | 0)), 70.0 + 410.0 / this.xMax * i_0, 550.0);
    }
  };
  CanvasState.prototype.clear = function () {
    this.context.fillStyle = '#D0D0D0';
    this.context.fillRect(0.0, 0.0, this.width, this.height);
    this.context.strokeStyle = '#000000';
    this.context.lineWidth = 4.0;
    this.context.strokeRect(0.0, 0.0, this.width, this.height);
    if (this.graph)
      this.drawGraph();
  };
  CanvasState.prototype.draw = function () {
    var tmp$;
    tmp$ = this.items.iterator();
    while (tmp$.hasNext()) {
      var item = tmp$.next();
      item.draw_v4m4ho$(this);
    }
  };
  CanvasState.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'CanvasState',
    interfaces: []
  };
  var lineString;
  function processInput(tokens) {
    var tmp$;
    if (matches(tokens[0], 'STOP')) {
      window.close();
    }modal.innerHTML = 'Program<br>';
    if (!matches(tokens[0], 'start')) {
      return generateError(0, "Program Does not Begin With 'start'", tokens);
    }if (!matches(tokens[tokens.length - 1 | 0], 'stop')) {
      return generateError(tokens.length - 1 | 0, "Program Does not End With 'stop'", tokens);
    }modal.innerHTML = modal.innerHTML + 'Program -&gt; start &lt;plot_data&gt; stop<br>';
    lineString = 'start &lt;plot_data&gt; stop<br>';
    var posIndex = 1;
    while (posIndex < tokens.length) {
      var last = true;
      tmp$ = tokens.length - 1 | 0;
      inner: for (var i = posIndex; i <= tmp$; i++) {
        if (matches(tokens[i], ';')) {
          last = false;
          break inner;
        }}
      if (last) {
        lineString = replace(lineString, '&lt;plot_data&gt;', '&lt;plot&gt;');
      } else {
        lineString = replace(lineString, '&lt;plot_data&gt;', '&lt;plot&gt; ; &lt;plot_data&gt;');
      }
      generateLine(lineString);
      if (matches(tokens[posIndex], 'bar') || matches(tokens[posIndex], 'edge')) {
        if ((posIndex + 4 | 0) > tokens.length) {
          return generateError(posIndex, 'Statement has not enough arguments.', tokens);
        }for (var i_0 = 1; i_0 < 4; i_0++) {
          if (matches(tokens[posIndex + i_0 | 0], ';') || matches(tokens[posIndex + i_0 | 0], 'stop'))
            return generateError(posIndex, 'Statement has not enough arguments.', tokens);
        }
        if (!(matches(tokens[posIndex + 4 | 0], ';') || matches(tokens[posIndex + 4 | 0], 'stop')))
          return generateError(posIndex, 'Statement has too many arguments.', tokens);
        if (!matches(tokens[posIndex + 2 | 0], ','))
          return generateError(posIndex, 'Incorrect Syntax.', tokens);
        var coord1 = tokens[posIndex + 1 | 0];
        var coord2 = tokens[posIndex + 3 | 0];
        if (matches(tokens[posIndex], 'bar')) {
          lineString = replace(lineString, '&lt;plot&gt;', 'bar &lt;x&gt;&lt;y&gt;,&lt;y&gt;');
          generateLine(lineString);
          if (!checkCoord(coord1, posIndex, tokens)) {
            return false;
          }if (coord2.length !== 1) {
            generateError(posIndex, 'Coord Y is not correct length', tokens);
            return false;
          }if (!checkY(coord2.charCodeAt(0))) {
            generateError(posIndex, 'Coord Y is not valid', tokens);
            return false;
          }lineString = replaceFirst(lineString, '&lt;y&gt;', String.fromCharCode(coord2.charCodeAt(0)));
          generateLine(lineString);
        } else {
          lineString = replace(lineString, '&lt;plot&gt;', 'edge &lt;x&gt;&lt;y&gt;,&lt;x&gt;&lt;y&gt;');
          generateLine(lineString);
          if (!checkCoord(coord1, posIndex, tokens)) {
            return false;
          }if (!checkCoord(coord2, posIndex, tokens)) {
            return false;
          }}
        posIndex = posIndex + 5 | 0;
      } else if (matches(tokens[posIndex], 'axis') || matches(tokens[posIndex], 'fill')) {
        if ((posIndex + 2 | 0) > tokens.length) {
          return generateError(posIndex, 'Statement has not enough arguments.', tokens);
        }if (matches(tokens[posIndex + 1 | 0], ';') || matches(tokens[posIndex + 1 | 0], 'stop'))
          return generateError(posIndex, 'Statement has not enough arguments.', tokens);
        if (!(matches(tokens[posIndex + 2 | 0], ';') || matches(tokens[posIndex + 2 | 0], 'stop')))
          return generateError(posIndex, 'Statement has too many arguments.', tokens);
        var coord = tokens[posIndex + 1 | 0];
        if (matches(tokens[posIndex], 'fill')) {
          lineString = replace(lineString, '&lt;plot&gt;', 'fill &lt;x&gt;&lt;y&gt;');
          generateLine(lineString);
          if (!checkCoord(coord, posIndex, tokens)) {
            return false;
          }} else {
          lineString = replace(lineString, '&lt;plot&gt;', 'axis &lt;x&gt;&lt;y&gt;');
          generateLine(lineString);
          if (!checkCoord(coord, posIndex, tokens)) {
            return false;
          }}
        posIndex = posIndex + 3 | 0;
      } else {
        return generateError(posIndex, 'This statement does not exist', tokens);
      }
    }
    return true;
  }
  function checkCoord(str, posIndex, tokens) {
    if (str.length !== 2) {
      generateError(posIndex, 'Incorrect Syntax of &lt;x&gt;&lt;y&gt;', tokens);
      return false;
    }if (!checkX(str.charCodeAt(0))) {
      generateError(posIndex, 'Coord X is not valid', tokens);
      return false;
    }lineString = replaceFirst(lineString, '&lt;x&gt;', String.fromCharCode(str.charCodeAt(0)));
    generateLine(lineString);
    if (!checkY(str.charCodeAt(1))) {
      generateError(posIndex, 'Coord Y is not valid', tokens);
      return false;
    }lineString = replaceFirst(lineString, '&lt;y&gt;', String.fromCharCode(str.charCodeAt(1)));
    generateLine(lineString);
    return true;
  }
  function checkX(char) {
    if (!contains('abcdefghij', char))
      return false;
    return true;
  }
  function checkY(char) {
    if (!contains('0123456789', char))
      return false;
    return true;
  }
  function generateLine(str) {
    var outputStr = 'Program -> ' + str + ' <br>';
    modal.innerHTML = modal.innerHTML + outputStr;
  }
  function generateError(errorPos, errorMessage, tokens) {
    var errorStr = '';
    var i = 0;
    while (!((errorPos + i | 0) > (tokens.length - 1 | 0) || matches(tokens[errorPos + i | 0], ';'))) {
      errorStr += tokens[errorPos + i | 0] + ' ';
      i = i + 1 | 0;
    }
    generateLine("<span style='color:red;'>" + errorMessage + '<\/span>');
    generateLine("<span style='color:red;'>" + errorStr + '<\/span>');
    derivationError = true;
    return false;
  }
  function parseString(str) {
    var $receiver = Regex_init('((?<=[;, ])|(?=[;, ]))').split_905azu$(str, 0);
    var destination = ArrayList_init();
    var tmp$;
    tmp$ = $receiver.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      if (!matches(element, ' '))
        destination.add_11rb$(element);
    }
    return copyToArray(destination);
  }
  function CanvasItem() {
    this.color = '#000';
    this.lineColor = '#FFF';
  }
  CanvasItem.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'CanvasItem',
    interfaces: []
  };
  function CanvasTextItem(str, x, y, parent) {
    CanvasItem.call(this);
    this.str = str;
    this.x = x;
    this.y = y;
    this.parent = parent;
    this.textSize = 1.0;
  }
  CanvasTextItem.prototype.draw_v4m4ho$ = function (state) {
    this.x -= 130;
    var context = state.context;
    context.save();
    context.lineWidth = this.textSize;
    context.strokeStyle = this.lineColor;
    context.beginPath();
    context.moveTo(this.x, this.y);
    if (this.parent != null) {
      context.lineTo(this.parent.x, this.parent.y);
    }context.stroke();
    context.strokeStyle = this.color;
    context.strokeText(this.str, this.x, this.y);
    context.restore();
  };
  CanvasTextItem.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'CanvasTextItem',
    interfaces: [CanvasItem]
  };
  function CanvasBarItem(coord1, coord2) {
    CanvasItem.call(this);
    this.coord1 = coord1;
    this.coord2 = coord2;
    this.x = toChar(this.coord1.charCodeAt(0) - 97) | 0;
    this.y = toDouble(String.fromCharCode(this.coord1.charCodeAt(1)));
    this.w = toInt(this.coord2);
  }
  CanvasBarItem.prototype.draw_v4m4ho$ = function (state) {
    var context = state.context;
    context.save();
    context.strokeStyle = this.color;
    context.lineWidth = 1.0;
    var height = 450.0 / state.yMax * this.y;
    context.strokeRect(50 + (400.0 / state.xMax + 1) * this.x, 500 - height, 450.0 / (state.xMax + 1 | 0) * this.w, height);
  };
  CanvasBarItem.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'CanvasBarItem',
    interfaces: [CanvasItem]
  };
  function CanvasEdgeItem(coord1, coord2) {
    CanvasItem.call(this);
    this.coord1 = coord1;
    this.coord2 = coord2;
    this.x = toChar(this.coord1.charCodeAt(0) - 97) | 0;
    this.y = toDouble(String.fromCharCode(this.coord1.charCodeAt(1)));
    this.x2 = toChar(this.coord2.charCodeAt(0) - 97) | 0;
    this.y2 = toDouble(String.fromCharCode(this.coord2.charCodeAt(1)));
  }
  CanvasEdgeItem.prototype.draw_v4m4ho$ = function (state) {
    var context = state.context;
    context.save();
    context.beginPath();
    context.strokeStyle = '000000';
    context.lineWidth = 1.0;
    context.moveTo(50 + (400.0 / state.xMax + 1) * this.x, 500 - 450.0 / state.yMax * this.y);
    context.lineTo(50 + (400.0 / state.xMax + 1) * this.x2, 500 - 450.0 / state.yMax * this.y2);
    context.stroke();
    context.closePath();
    context.restore();
  };
  CanvasEdgeItem.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'CanvasEdgeItem',
    interfaces: [CanvasItem]
  };
  function CanvasFillItem(coord) {
    CanvasItem.call(this);
    this.coord = coord;
    this.x = toChar(this.coord.charCodeAt(0) - 97) | 0;
    this.y = toDouble(String.fromCharCode(this.coord.charCodeAt(1)));
  }
  CanvasFillItem.prototype.getPixelPos_lu1900$ = function (x, y) {
    return numberToInt((y * canvas.width + x) * 4);
  };
  CanvasFillItem.prototype.matchStartColor_zdkteq$ = function (data, pos, startColor) {
    var tmp$ = data[pos + 0 | 0] === startColor[0];
    if (tmp$) {
      tmp$ = data[pos + 1 | 0] === startColor[1];
    }var tmp$_0 = tmp$;
    if (tmp$_0) {
      tmp$_0 = data[pos + 2 | 0] === startColor[2];
    }var tmp$_1 = tmp$_0;
    if (tmp$_1) {
      tmp$_1 = data[pos + 3 | 0] === startColor[3];
    }return tmp$_1;
  };
  CanvasFillItem.prototype.colorPixel_th4t9b$ = function (data, pos, color) {
    data[pos] = toByte(color[0]);
    data[pos + 1 | 0] = toByte(color[1]);
    data[pos + 2 | 0] = toByte(color[2]);
    data[pos + 3 | 0] = toByte(color[3]);
    return data;
  };
  CanvasFillItem.prototype.draw_v4m4ho$ = function (state) {
    var tmp$, tmp$_0;
    var context = state.context;
    var width = context.canvas.width;
    var dstImg = context.getImageData(0.0, 0.0, canvas.width * 1.0, canvas.height * 1.0);
    var dstData = dstImg.data;
    var startCol = new Uint8ClampedArray(4);
    startCol[0] = 208;
    startCol[1] = 208;
    startCol[2] = 208;
    startCol[3] = 255;
    var startColor = startCol;
    var fillColor = new Uint8ClampedArray(4);
    fillColor[0] = 255;
    fillColor[1] = 255;
    fillColor[2] = 255;
    fillColor[3] = 255;
    var list = ArrayList_init_0(1);
    for (var index = 0; index < 1; index++) {
      list.add_11rb$(new Pos(50 + ((400 / state.xMax | 0) + 1 | 0) * this.x, 500 - (450 / state.yMax | 0) * this.y));
    }
    var todo = list;
    while (todo.size !== 0) {
      var pos = removeLast(todo);
      var x = pos.x;
      var y = pos.y;
      var currentPos = this.getPixelPos_lu1900$(x, y);
      while ((tmp$ = y, y = tmp$ - 1, tmp$) >= 0 && this.matchStartColor_zdkteq$(dstData, currentPos, startColor)) {
        currentPos = currentPos - (width * 4 | 0) | 0;
      }
      currentPos = currentPos + (width * 4 | 0) | 0;
      y = y + 1;
      var reachLeft = false;
      var reachRight = false;
      while ((tmp$_0 = y, y = tmp$_0 + 1, tmp$_0) < (canvas.height - 1 | 0) && this.matchStartColor_zdkteq$(dstData, currentPos, startColor)) {
        var dstTemp = dstData;
        dstData = this.colorPixel_th4t9b$(dstData, currentPos, fillColor);
        if (x > 0) {
          if (this.matchStartColor_zdkteq$(dstData, currentPos - 4 | 0, startColor)) {
            if (!reachLeft) {
              todo.add_11rb$(new Pos(x - 1, y));
              reachLeft = true;
            }} else if (reachLeft) {
            reachLeft = false;
          }}if (x < (canvas.width - 1 | 0)) {
          if (this.matchStartColor_zdkteq$(dstData, currentPos + 4 | 0, startColor)) {
            if (!reachRight) {
              todo.add_11rb$(new Pos(x + 1, y));
              reachRight = true;
            }} else if (reachRight) {
            reachRight = false;
          }}currentPos = currentPos + (width * 4 | 0) | 0;
      }
    }
    context.putImageData(dstImg, 0.0, 0.0);
  };
  CanvasFillItem.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'CanvasFillItem',
    interfaces: [CanvasItem]
  };
  function generateVisuals(tokens) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2, tmp$_3, tmp$_4;
    var width = canvasState.width;
    var y = {v: 40.0};
    while (!parsedItems.isEmpty()) {
      parsedItems.removeAt_za3lpa$(0);
    }
    var list = ArrayList_init_0(1);
    for (var index = 0; index < 1; index++) {
      list.add_11rb$(new CanvasTextItem('Program', width / 2, y.v, null));
    }
    var arrOfPlot = {v: list};
    y.v += 20;
    arrOfPlot.v.add_11rb$(new CanvasTextItem('start', width / 2 - 100, y.v, arrOfPlot.v.get_za3lpa$(0)));
    arrOfPlot.v.add_11rb$(new CanvasTextItem('<plot_data>', width / 2, y.v, arrOfPlot.v.get_za3lpa$(0)));
    arrOfPlot.v.add_11rb$(new CanvasTextItem('stop', width / 2 + 100, y.v, arrOfPlot.v.get_za3lpa$(0)));
    y.v += 20;
    tmp$ = tokens.length - 1 | 0;
    for (var i = 0; i < tmp$; i++) {
      if (matches(tokens[i], ';')) {
        tmp$_0 = asReversed(arrOfPlot.v).iterator();
        inner: while (tmp$_0.hasNext()) {
          var k = tmp$_0.next();
          if (matches(k.str, '<plot_data>')) {
            arrOfPlot.v.add_11rb$(new CanvasTextItem('<plot>', k.x - 100, y.v, k));
            arrOfPlot.v.add_11rb$(new CanvasTextItem(';', k.x, y.v, k));
            arrOfPlot.v.add_11rb$(new CanvasTextItem('<plot_data>', k.x + 100, y.v, k));
            y.v += 20;
            break inner;
          }}
      }}
    tmp$_1 = asReversed(arrOfPlot.v).iterator();
    while (tmp$_1.hasNext()) {
      var k_0 = tmp$_1.next();
      if (matches(k_0.str, '<plot_data>')) {
        arrOfPlot.v.add_11rb$(new CanvasTextItem('<plot>', k_0.x, y.v, k_0));
        y.v += 20;
        break;
      }}
    var list_0 = ArrayList_init_0(0);
    for (var index_0 = 0; index_0 < 0; index_0++) {
      list_0.add_11rb$(arrOfPlot.v.get_za3lpa$(0));
    }
    var plotPos = list_0;
    tmp$_2 = arrOfPlot.v.iterator();
    while (tmp$_2.hasNext()) {
      var item = tmp$_2.next();
      if (matches(item.str, '<plot>')) {
        plotPos.add_11rb$(item);
      }}
    var posIndex = 1;
    var counter = 0;
    while (posIndex < (tokens.length - 1 | 0)) {
      var plot = plotPos.get_za3lpa$((tmp$_3 = counter, counter = tmp$_3 + 1 | 0, tmp$_3));
      if (matches(tokens[posIndex], 'bar') || matches(tokens[posIndex], 'edge')) {
        var coord1 = tokens[posIndex + 1 | 0];
        var coord2 = tokens[posIndex + 3 | 0];
        if (matches(tokens[posIndex], 'bar')) {
          arrOfPlot.v.add_11rb$(new CanvasTextItem('bar', ensureNotNull(plot).x - 50, y.v, plot));
          arrOfPlot.v.add_11rb$(new CanvasTextItem('<y>', plot.x + 20, y.v, plot));
          arrOfPlot.v.add_11rb$(new CanvasTextItem(String.fromCharCode(coord2.charCodeAt(0)), plot.x + 20, y.v + 20, last(arrOfPlot.v)));
          parsedItems.add_11rb$(new CanvasBarItem(coord1, coord2));
        } else {
          arrOfPlot.v.add_11rb$(new CanvasTextItem('edge', ensureNotNull(plot).x - 55, y.v, plot));
          arrOfPlot.v.add_11rb$(new CanvasTextItem('<x>', plot.x + 20, y.v, plot));
          arrOfPlot.v.add_11rb$(new CanvasTextItem(String.fromCharCode(coord2.charCodeAt(0)), plot.x + 20, y.v + 20, last(arrOfPlot.v)));
          arrOfPlot.v.add_11rb$(new CanvasTextItem('<y>', plot.x + 40, y.v, plot));
          arrOfPlot.v.add_11rb$(new CanvasTextItem(String.fromCharCode(coord2.charCodeAt(1)), plot.x + 40, y.v + 20, last(arrOfPlot.v)));
          parsedItems.add_11rb$(new CanvasEdgeItem(coord1, coord2));
        }
        arrOfPlot.v.add_11rb$(new CanvasTextItem('<x>', plot.x - 30, y.v, plot));
        arrOfPlot.v.add_11rb$(new CanvasTextItem(String.fromCharCode(coord1.charCodeAt(0)), plot.x - 30, y.v + 20, last(arrOfPlot.v)));
        arrOfPlot.v.add_11rb$(new CanvasTextItem('<y>', plot.x - 10, y.v, plot));
        arrOfPlot.v.add_11rb$(new CanvasTextItem(String.fromCharCode(coord1.charCodeAt(1)), plot.x - 10, y.v + 20, last(arrOfPlot.v)));
        arrOfPlot.v.add_11rb$(new CanvasTextItem(',', plot.x + 10, y.v, plot));
        y.v += 20;
        posIndex = posIndex + 5 | 0;
      } else if (matches(tokens[posIndex], 'axis') || matches(tokens[posIndex], 'fill')) {
        var coord = {v: tokens[posIndex + 1 | 0]};
        if (matches(tokens[posIndex], 'fill')) {
          arrOfPlot.v.add_11rb$(new CanvasTextItem('fill', ensureNotNull(plot).x - 20, y.v, plot));
          parsedItems.add_11rb$(new CanvasFillItem(coord.v));
        } else {
          arrOfPlot.v.add_11rb$(new CanvasTextItem('axis', ensureNotNull(plot).x - 20, y.v, plot));
          canvasState.setAxis_vux9f0$((coord.v.charCodeAt(0) | 0) - 97 | 0, toInt(String.fromCharCode(coord.v.charCodeAt(1))));
        }
        arrOfPlot.v.add_11rb$(new CanvasTextItem('<x>', plot.x, y.v, plot));
        arrOfPlot.v.add_11rb$(new CanvasTextItem(String.fromCharCode(coord.v.charCodeAt(0)), plot.x, y.v + 20, last(arrOfPlot.v)));
        arrOfPlot.v.add_11rb$(new CanvasTextItem('<y>', plot.x + 20, y.v, plot));
        arrOfPlot.v.add_11rb$(new CanvasTextItem(String.fromCharCode(coord.v.charCodeAt(1)), plot.x + 20, y.v + 20, last(arrOfPlot.v)));
        y.v += 20;
        posIndex = posIndex + 3 | 0;
      }}
    tmp$_4 = arrOfPlot.v.iterator();
    while (tmp$_4.hasNext()) {
      var item_0 = tmp$_4.next();
      canvasState.addItem_cgrhq8$(item_0);
    }
  }
  function continueFlow(canvasState) {
    switch (flowState) {
      case 0:
        tokens = parseString(inp.value);
        canvas.hidden = true;
        derivationError = false;
        processInput(tokens);
        modalDiv.style.display = 'block';
        modal.style.display = 'block';
        flowState = flowState + 1 | 0;
        break;
      case 1:
        modal.style.display = 'none';
        if (!derivationError) {
          canvas.hidden = false;
          canvasState.emptyItems();
          canvasState.notGraph();
          canvasState.clear();
          generateVisuals(tokens);
          canvasState.draw();
          flowState = flowState + 1 | 0;
        } else {
          modalDiv.style.display = 'none';
          flowState = 0;
        }

        break;
      case 2:
        canvasState.emptyItems();
        canvasState.isGraph();
        canvasState.clear();
        canvasState.addItems_10kp50$(parsedItems);
        canvasState.draw();
        flowState = flowState + 1 | 0;
        break;
      case 3:
        modalDiv.style.display = 'none';
        flowState = 0;
        break;
    }
  }
  function main$lambda$lambda(it) {
    continueFlow(canvasState);
    return Unit;
  }
  function main$lambda$lambda_0(it) {
    continueFlow(canvasState);
    return Unit;
  }
  function main$lambda(it) {
    var tmp$;
    modalDiv.style.display = 'none';
    modalDiv.classList.add('modal');
    ensureNotNull(document.body).appendChild(modalDiv);
    var contButton = Kotlin.isType(tmp$ = document.createElement('button'), HTMLButtonElement) ? tmp$ : throwCCE();
    contButton.innerText = 'Continue';
    ensureNotNull(modalDiv).appendChild(contButton);
    modalCont.classList.add('modal-content');
    ensureNotNull(modalCont).appendChild(modal);
    ensureNotNull(modalDiv).appendChild(modalCont);
    get_context().canvas.width = 600;
    get_context().canvas.height = 600;
    ensureNotNull(modalCont).appendChild(canvas);
    inp.placeholder = 'Enter Code Here';
    inp.type = 'String';
    ensureNotNull(document.getElementById('InputGoesHere')).appendChild(inp);
    button.innerText = 'Input';
    ensureNotNull(document.getElementById('ButtonGoesHere')).appendChild(button);
    canvasState = new CanvasState(canvas);
    contButton.addEventListener('click', main$lambda$lambda);
    button.addEventListener('click', main$lambda$lambda_0);
    return Unit;
  }
  function main(args) {
    window.onload = main$lambda;
  }
  var package$project2 = _.project2 || (_.project2 = {});
  Object.defineProperty(package$project2, 'inp', {
    get: function () {
      return inp;
    }
  });
  Object.defineProperty(package$project2, 'button', {
    get: function () {
      return button;
    }
  });
  Object.defineProperty(package$project2, 'canvas', {
    get: function () {
      return canvas;
    }
  });
  Object.defineProperty(package$project2, 'modalCont', {
    get: function () {
      return modalCont;
    }
  });
  Object.defineProperty(package$project2, 'modal', {
    get: function () {
      return modal;
    }
  });
  Object.defineProperty(package$project2, 'modalDiv', {
    get: function () {
      return modalDiv;
    }
  });
  Object.defineProperty(package$project2, 'tokens', {
    get: function () {
      return tokens;
    },
    set: function (value) {
      tokens = value;
    }
  });
  Object.defineProperty(package$project2, 'flowState', {
    get: function () {
      return flowState;
    },
    set: function (value) {
      flowState = value;
    }
  });
  Object.defineProperty(package$project2, 'derivationError', {
    get: function () {
      return derivationError;
    },
    set: function (value) {
      derivationError = value;
    }
  });
  Object.defineProperty(package$project2, 'parsedItems', {
    get: function () {
      return parsedItems;
    },
    set: function (value) {
      parsedItems = value;
    }
  });
  Object.defineProperty(package$project2, 'canvasState', {
    get: function () {
      return canvasState;
    },
    set: function (value) {
      canvasState = value;
    }
  });
  Object.defineProperty(package$project2, 'context', {
    get: get_context
  });
  package$project2.Pos = Pos;
  package$project2.CanvasState = CanvasState;
  Object.defineProperty(package$project2, 'lineString', {
    get: function () {
      return lineString;
    },
    set: function (value) {
      lineString = value;
    }
  });
  package$project2.processInput_kand9s$ = processInput;
  package$project2.checkCoord_s50ims$ = checkCoord;
  package$project2.checkX_s8itvh$ = checkX;
  package$project2.checkY_s8itvh$ = checkY;
  package$project2.generateLine_61zpoe$ = generateLine;
  package$project2.generateError_6wwcag$ = generateError;
  package$project2.parseString_61zpoe$ = parseString;
  package$project2.CanvasItem = CanvasItem;
  package$project2.CanvasTextItem = CanvasTextItem;
  package$project2.CanvasBarItem = CanvasBarItem;
  package$project2.CanvasEdgeItem = CanvasEdgeItem;
  package$project2.CanvasFillItem = CanvasFillItem;
  package$project2.generateVisuals_kand9s$ = generateVisuals;
  package$project2.continueFlow_v4m4ho$ = continueFlow;
  package$project2.main_kand9s$ = main;
  var tmp$, tmp$_0, tmp$_1, tmp$_2, tmp$_3, tmp$_4;
  inp = Kotlin.isType(tmp$ = document.createElement('input'), HTMLInputElement) ? tmp$ : throwCCE();
  button = Kotlin.isType(tmp$_0 = document.createElement('button'), HTMLButtonElement) ? tmp$_0 : throwCCE();
  canvas = Kotlin.isType(tmp$_1 = document.createElement('canvas'), HTMLCanvasElement) ? tmp$_1 : throwCCE();
  modalCont = Kotlin.isType(tmp$_2 = document.createElement('div'), HTMLDivElement) ? tmp$_2 : throwCCE();
  modal = Kotlin.isType(tmp$_3 = document.createElement('div'), HTMLDivElement) ? tmp$_3 : throwCCE();
  modalDiv = Kotlin.isType(tmp$_4 = document.createElement('div'), HTMLDivElement) ? tmp$_4 : throwCCE();
  var array = Array_0(0);
  var tmp$_5;
  tmp$_5 = array.length - 1 | 0;
  for (var i = 0; i <= tmp$_5; i++) {
    array[i] = '';
  }
  tokens = array;
  flowState = 0;
  derivationError = false;
  var list = ArrayList_init_0(0);
  for (var index = 0; index < 0; index++) {
    list.add_11rb$(new CanvasBarItem('  ', '  '));
  }
  parsedItems = list;
  canvasState = new CanvasState(canvas);
  lineString = '';
  main([]);
  Kotlin.defineModule('KotlinJS', _);
  return _;
}(typeof KotlinJS === 'undefined' ? {} : KotlinJS, kotlin);
