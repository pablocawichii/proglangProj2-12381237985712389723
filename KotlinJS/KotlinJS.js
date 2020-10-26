if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'KotlinJS'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'KotlinJS'.");
}var KotlinJS = function (_, Kotlin) {
  'use strict';
  var throwCCE = Kotlin.throwCCE;
  var Unit = Kotlin.kotlin.Unit;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var matches = Kotlin.kotlin.text.matches_rjktp$;
  var replace = Kotlin.kotlin.text.replace_680rmw$;
  var replaceFirst = Kotlin.kotlin.text.replaceFirst_680rmw$;
  var contains = Kotlin.kotlin.text.contains_sgbm27$;
  var Regex_init = Kotlin.kotlin.text.Regex_init_61zpoe$;
  var ensureNotNull = Kotlin.ensureNotNull;
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  var copyToArray = Kotlin.kotlin.collections.copyToArray;
  var Array_0 = Array;
  var inp;
  var button;
  var canvas;
  var derivation;
  function get_context() {
    var tmp$;
    return Kotlin.isType(tmp$ = canvas.getContext('2d'), CanvasRenderingContext2D) ? tmp$ : throwCCE();
  }
  function CanvasState(canvas) {
    this.canvas = canvas;
    this.width = this.canvas.width;
    this.height = this.canvas.height;
    this.context = get_context();
    this.changed = true;
    this.interval = 33;
    this.xMax = 10;
    this.yMax = 10;
    this.graph = false;
    window.setInterval(CanvasState_init$lambda(this), this.interval);
  }
  CanvasState.prototype.isGraph = function () {
    this.graph = true;
  };
  CanvasState.prototype.notGraph = function () {
    this.graph = true;
  };
  CanvasState.prototype.drawGraph = function () {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    this.context.strokeStyle = '#000000';
    this.context.lineWidth = 1.0;
    this.context.strokeText('Y-Axis', 35.0, 35.0);
    this.context.fillStyle = '#000000';
    this.context.fillRect(50.0, 50.0, 1.0, 450.0);
    this.context.strokeStyle = '#000000';
    this.context.lineWidth = 1.0;
    this.context.strokeText('X-Axis', 515.0, 500.0);
    this.context.fillStyle = '#000000';
    this.context.fillRect(50.0, 500.0, 450.0, 1.0);
    if (this.yMax >= 20) {
      var k = 0;
      tmp$ = this.yMax + 1 | 0;
      for (var i = 0; i < tmp$; i++) {
        if (i % (this.yMax / 10 | 0) === 0) {
          this.context.strokeStyle = '#000000';
          this.context.lineWidth = 1.0;
          this.context.strokeText(i.toString(), 25.0, 500.0 - 450.0 / this.yMax * k);
          k = k + (this.yMax / 10 | 0) | 0;
        }}
    } else {
      var k_0 = 0;
      tmp$_0 = this.yMax + 1 | 0;
      for (var i_0 = 0; i_0 < tmp$_0; i_0++) {
        this.context.strokeStyle = '#000000';
        this.context.lineWidth = 1.0;
        this.context.strokeText(i_0.toString(), 25.0, 500.0 - 450.0 / this.yMax * k_0);
        k_0 = k_0 + 1 | 0;
      }
    }
    if (this.xMax >= 20) {
      var k_1 = 0;
      tmp$_1 = this.xMax + 1 | 0;
      for (var i_1 = 0; i_1 < tmp$_1; i_1++) {
        if (i_1 % (this.xMax / 10 | 0) === 0) {
          this.context.strokeStyle = '#000000';
          this.context.lineWidth = 1.0;
          this.context.strokeText(i_1.toString(), 50.0 + 450.0 / this.xMax * k_1, 550.0);
          k_1 = k_1 + (this.xMax / 10 | 0) | 0;
        }}
    } else {
      var k_2 = 0;
      tmp$_2 = this.xMax + 1 | 0;
      for (var i_2 = 0; i_2 < tmp$_2; i_2++) {
        this.context.strokeStyle = '#000000';
        this.context.lineWidth = 1.0;
        this.context.strokeText(i_2.toString(), 50.0 + 450.0 / this.xMax * k_2, 550.0);
        k_2 = k_2 + 1 | 0;
      }
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
    if (!this.changed)
      return;
    this.changed = false;
    this.clear();
  };
  function CanvasState_init$lambda(this$CanvasState) {
    return function () {
      this$CanvasState.draw();
      return Unit;
    };
  }
  CanvasState.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'CanvasState',
    interfaces: []
  };
  var lineString;
  function processInput(tokens) {
    var tmp$;
    derivation.innerHTML = 'Program<br>';
    if (!matches(tokens[0], 'start')) {
      return generateError(0, "Program Does not Begin With 'start'", tokens);
    }if (!matches(tokens[tokens.length - 1 | 0], 'stop')) {
      return generateError(tokens.length - 1 | 0, "Program Does not End With 'stop'", tokens);
    }derivation.innerHTML = derivation.innerHTML + 'Program -&gt; start &lt;plot_data&gt; stop<br>';
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
    var tmp$;
    var outputStr = 'Program -> ' + str + ' <br>';
    derivation.innerHTML = derivation.innerHTML + outputStr;
    (Kotlin.isType(tmp$ = document.getElementById('derivation'), HTMLDivElement) ? tmp$ : throwCCE()).innerHTML = derivation.innerHTML;
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
  function parseTree(tokens) {
    var tmp$, tmp$_0;
    var parseTreeDiv = Kotlin.isType(tmp$ = document.getElementById('parseTree'), HTMLDivElement) ? tmp$ : throwCCE();
    canvas.hidden = false;
    ensureNotNull(parseTreeDiv).appendChild(canvas);
    var canvasState = new CanvasState(canvas);
    var array = Array_0(1);
    var tmp$_1;
    tmp$_1 = array.length - 1 | 0;
    for (var i = 0; i <= tmp$_1; i++) {
      var array_0 = Array_0(1);
      var tmp$_2;
      tmp$_2 = array_0.length - 1 | 0;
      for (var i_0 = 0; i_0 <= tmp$_2; i_0++) {
        array_0[i_0] = 'Program';
      }
      array[i] = array_0;
    }
    var htmlString = array;
    console.log(htmlString);
    tmp$_0 = tokens.length - 1 | 0;
    inner: for (var i_1 = 1; i_1 <= tmp$_0; i_1++) {
      matches(tokens[i_1], ';');
    }
    var posIndex = 1;
    while (posIndex < (tokens.length - 1 | 0)) {
      if (matches(tokens[posIndex], 'bar') || matches(tokens[posIndex], 'edge')) {
        var coord1 = tokens[posIndex + 1 | 0];
        var coord2 = tokens[posIndex + 3 | 0];
        matches(tokens[posIndex], 'bar');
        posIndex = posIndex + 5 | 0;
      } else if (matches(tokens[posIndex], 'axis') || matches(tokens[posIndex], 'fill')) {
        var coord = tokens[posIndex + 1 | 0];
        matches(tokens[posIndex], 'fill');
        posIndex = posIndex + 3 | 0;
      }}
    console.log('Asdf');
  }
  function main$lambda$lambda(closure$parseTreeDiv) {
    return function (it) {
      var tokens = parseString(inp.value);
      console.log(tokens);
      if (!processInput(tokens)) {
        closure$parseTreeDiv.innerHTML = '<p> Parse Tree could not be created <\/p>';
        canvas.hidden = true;
      } else {
        parseTree(tokens);
        canvas.hidden = false;
      }
      return Unit;
    };
  }
  function main$lambda(it) {
    var tmp$, tmp$_0, tmp$_1;
    var context = Kotlin.isType(tmp$ = canvas.getContext('2d'), CanvasRenderingContext2D) ? tmp$ : throwCCE();
    context.canvas.width = 600;
    context.canvas.height = 600;
    context.canvas.hidden = true;
    ensureNotNull(document.body).appendChild(canvas);
    inp.placeholder = 'Enter Code Here';
    inp.type = 'String';
    ensureNotNull(document.body).appendChild(inp);
    button.innerText = 'Input';
    ensureNotNull(document.body).appendChild(button);
    var parseTreeDiv = Kotlin.isType(tmp$_0 = document.getElementById('parseTree'), HTMLDivElement) ? tmp$_0 : throwCCE();
    var canvasState = new CanvasState(canvas);
    (Kotlin.isType(tmp$_1 = document.getElementById('derivation'), HTMLDivElement) ? tmp$_1 : throwCCE()).innerHTML = 'Derivation';
    button.addEventListener('click', main$lambda$lambda(parseTreeDiv));
    console.log(parseString('This is a string; XD'));
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
  Object.defineProperty(package$project2, 'derivation', {
    get: function () {
      return derivation;
    },
    set: function (value) {
      derivation = value;
    }
  });
  Object.defineProperty(package$project2, 'context', {
    get: get_context
  });
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
  package$project2.parseTree_kand9s$ = parseTree;
  package$project2.main_kand9s$ = main;
  var tmp$, tmp$_0, tmp$_1, tmp$_2;
  inp = Kotlin.isType(tmp$ = document.createElement('input'), HTMLInputElement) ? tmp$ : throwCCE();
  button = Kotlin.isType(tmp$_0 = document.createElement('button'), HTMLButtonElement) ? tmp$_0 : throwCCE();
  canvas = Kotlin.isType(tmp$_1 = document.createElement('canvas'), HTMLCanvasElement) ? tmp$_1 : throwCCE();
  derivation = Kotlin.isType(tmp$_2 = document.createElement('div'), HTMLDivElement) ? tmp$_2 : throwCCE();
  lineString = '';
  main([]);
  Kotlin.defineModule('KotlinJS', _);
  return _;
}(typeof KotlinJS === 'undefined' ? {} : KotlinJS, kotlin);
