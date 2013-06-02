var fs = require('fs');
var ts = require("typescript");
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

function processModuleTree(moduleTree, indent) {
  for (var moduleName in moduleTree) {
    kt += "\n" + indent + "object " + moduleName + " {"
    var subModuleTree = Object.create(null)
    var subIndent = indent + "\t";
    var subModules = moduleTree[moduleName];
    for (var i = 0, n = subModules.length; i < n; i++) {
      processMembers(subModules[i], subModuleTree, subIndent)
    }
    processModuleTree(subModuleTree, subIndent)
  }
}

function processMembers(members, moduleTree, indent) {
  for (var i = 0, n = members.length; i < n; i++) {
    var member = members[i]
    if (!member || !member.name) {
      name = ""
    }

    var name = member.name.text
    switch (member.nodeType) {
      case ts.TypeScript.NodeType.FuncDecl:
        kt += "\n" + indent + "public fun " + name + "()"
        break;

      case ts.TypeScript.NodeType.ModuleDeclaration:
        // collect modules, d.ts can contains "module kendo, module kendo.data", but in Kotlin it will be "object kendo {object data{}}"
        // so, we cannot print module memebers rignt now
        var entry = moduleTree[name]
        if (entry === undefined) {
          moduleTree[name] = [member.members.members]
        }
        else {
          moduleTree[name].push(member.members.members)
        }
        break;

      default:
        console.log("skip " + name + ", unsupported node type")
    }
  }
}
