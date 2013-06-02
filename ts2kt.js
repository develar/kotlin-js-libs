var fs = require('fs');
var assert = require('assert');
var ts = require("./typescript");
//eval(fs.readFileSync('node_modules/typescript/bin/typescript.js')+'');

var compiler = new ts.TypeScript.TypeScriptCompiler();
compiler.parseUnit(fs.readFileSync('/Users/develar/Downloads/kendoui/typescript/kendo.web.d.ts', {encoding: "utf8"}), "kendo");
var topLevelMemebers = compiler.scripts.members[0].bod.members;
// todo configure package name
var kt = "package kendo"

var rootModuleTree = Object.create(null)
processMembers(topLevelMemebers, rootModuleTree, "")
processModuleTree(rootModuleTree, "")
fs.writeFileSync("out.kt", kt);

function appendNewLineIfNeed() {
  if (kt[kt.length - 1] != "{") {
    kt += "\n"
  }
}
function processModuleTree(moduleTree, indent) {
  for (var moduleName in moduleTree) {
    appendNewLineIfNeed()
    kt += "\n" + indent + "object " + moduleName + " {"
    var subModuleTree = Object.create(null)
    var subIndent = indent + "\t";
    var subModules = moduleTree[moduleName];
    for (var i = 0, n = subModules.length; i < n; i++) {
      processMembers(subModules[i], subModuleTree, subIndent)
    }
    processModuleTree(subModuleTree, subIndent)
    kt += "\n" + indent + "}"
  }
}

function processFunction(member, indent) {
  if (indent !== null) {
    kt += "\n" + indent + "public fun " + member.name.text
  }
  kt += "("
  var args = member.arguments.members
  var isFirst = true
  for (var i = 0, n = args.length; i < n; i++) {
    var arg = args[i];
    if (arg.nodeType != ts.TypeScript.NodeType.ArgDecl) {
      console.log("skip unsupported arg node " + arg)
      continue
    }

    if (isFirst) {
      isFirst = false
    }
    else {
      kt += ", "
    }

    kt += arg.id.text + ":"
    processExpression(arg.typeExpr.term)

    //drebugger
  }
  kt += "):"
  if (member.returnTypeAnnotation === null) {
    kt += "Unit"
  }
  else {
    processExpression(member.returnTypeAnnotation.term)
  }
}

function tsTypeNameToKotlin(name) {
  switch (name) {
    case "string":
      return "String"

    case "any":
      return "Any"

    case "void":
      return "Unit"

    default:
      return name
  }
}

function processExpression(expression) {
  switch (expression.nodeType) {
    case ts.TypeScript.NodeType.Dot:
      processExpression(expression.operand1)
      kt += "."
      processExpression(expression.operand2)
      break

    case ts.TypeScript.NodeType.Name:
      kt += tsTypeNameToKotlin(expression.text)
      break

    case ts.TypeScript.NodeType.FuncDecl:
      processFunction(expression, null)
      break

    case ts.TypeScript.NodeType.InterfaceDeclaration:
      // todo ObservableObject.toJSON(): { [key: string]: any; };
      kt += "Any"
      break

    default:
      debugger
      throw new Error("Unsupported node " + expression.nodeType)
  }
}

function processMembers(members, moduleTree, indent) {
  for (var i = 0, n = members.length; i < n; i++) {
    var member = members[i]

    // d.ts can contains duplicated class/interface, so, we merge it
    var traitNameToMembers = Object.create(null)
    var classNameToMembers = Object.create(null)
    switch (member.nodeType) {
      case ts.TypeScript.NodeType.FuncDecl:
        processFunction(member, indent)
        break;

      case ts.TypeScript.NodeType.InterfaceDeclaration:
        addOrCreate(traitNameToMembers, member.name.text, member.members.members)
        break;

      case ts.TypeScript.NodeType.ClassDeclaration:
        addOrCreate(classNameToMembers, member.name.text, member.members.members)
        break;

      case ts.TypeScript.NodeType.ModuleDeclaration:
        assert(moduleTree !== null)
        var name = member.name.text
        // collect modules, d.ts can contains "module kendo, module kendo.data", but in Kotlin it will be "object kendo {object data{}}"
        // so, we cannot print module memebers rignt now
        addOrCreate(moduleTree, name, member.members.members)
        break;

      default:
        console.log("skip " + (member.name === undefined ? "" : member.name.text) + ", unsupported node type " + member.nodeType)
    }

    processClassesOrTraits(traitNameToMembers, indent, true)
    processClassesOrTraits(classNameToMembers, indent, false)
  }
}

function processClassesOrTraits(map, indent, isTrait) {
  for (var name in map) {
    appendNewLineIfNeed()
    kt += "\n" + indent + "public " + (isTrait ? "trait" : "class") + " " + name
    var subIndent = indent + "\t";
    var subMembers = map[name];
    var hasMembers = subMembers.length > 0 && subMembers[0].length > 0;
    if (hasMembers) {
      kt += " {"
    }
    for (var i = 0, n = subMembers.length; i < n; i++) {
      processMembers(subMembers[i], null, subIndent)
    }

    if (hasMembers) {
      kt += "\n" + indent + "}"
    }
  }
}

function addOrCreate(map, name, item) {
  var list = map[name]
  if (list === undefined) {
    map[name] = [item]
  }
  else {
    map[name].push(item)
  }
}