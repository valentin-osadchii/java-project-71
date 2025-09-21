[![Actions Status](https://github.com/valentin-osadchii/java-project-71/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/valentin-osadchii/java-project-71/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=valentin-osadchii_java-project-71&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=valentin-osadchii_java-project-71)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=valentin-osadchii_java-project-71&metric=coverage)](https://sonarcloud.io/summary/new_code?id=valentin-osadchii_java-project-71)

Command-line приложение на Java для сравнения двух файлов и отображения изменений между ними
Поддерживает два формата файлов:
- json
- yaml

Форматы файлов должны совпадать.

Вывод результата возможен в трех форматах:
- stylish (по умолчанию)
- plain
- json

Сравнение происходит не рекурсивно: изменения во вложенных структурах (объектах) не обрабатываются.
Содерждимое таких объектов отображается как [complex value].

## Пример использования

### Справка

```bash
$  ./app -h

Usage: gendiff [-hV] [-f=<format>] <filepath1> <filepath2>
Compares two configuration files and shows a difference.
      <filepath1>         path to first file
      <filepath2>         path to second file
  -f, --format=<format>   output format [default: stylish]
  -h, --help              Show this help message and exit.
  -V, --version           Print version information and exit.
```

### Сравнение json, вывод в формате по-умолчанию

```bash
$  ./build/install/app/bin/app examples/file1nested.json examples/file2nested.json

{
    chars1: [a, b, c]
  - chars2: [d, e, f]
  + chars2: false
  - checked: false
  + checked: true
  - default: null
  + default: [value1, value2]
  - id: 45
  + id: null
  - key1: value1
  + key2: value2
    numbers1: [1, 2, 3, 4]
  - numbers2: [2, 3, 4, 5]
  + numbers2: [22, 33, 44, 55]
  - numbers3: [3, 4, 5]
  + numbers4: [4, 5, 6]
  + obj1: {nestedKey=value, isNested=true}
  - setting1: Some value
  + setting1: Another value
  - setting2: 200
  + setting2: 300
  - setting3: true
  + setting3: none
}

```

### Сравнение yaml, вывод в plain-формате

```bash
$  ./build/install/app/bin/app -f plain examples/file1nested.yaml examples/file2nested.yaml

Property 'chars2' was updated. From [complex value] to false
Property 'checked' was updated. From false to true
Property 'default' was updated. From null to [complex value]
Property 'id' was updated. From 45 to null
Property 'key1' was removed
Property 'key2' was added with value: 'value2'
Property 'numbers2' was updated. From [complex value] to [complex value]
Property 'numbers3' was removed
Property 'numbers4' was added with value: [complex value]
Property 'obj1' was added with value: [complex value]
Property 'setting1' was updated. From 'Some value' to 'Another value'
Property 'setting2' was updated. From 200 to 300
Property 'setting3' was updated. From true to 'none'
```


### Сравнение yaml, вывод в json-формате

```bash
$  ./build/install/app/bin/app -f json examples/file1nested.yaml examples/file2nested.yaml

[{"key":"chars1","status":"UNCHANGED","oldValue":["a","b","c"],"newValue":["a","b","c"]},{"key":"chars2","status":"CHANGED","oldValue":["d","e","f"],"newValue":false},{"key":"checked","status":"CHANGED","oldValue":false,"newValue":true},{"key":"default","status":"CHANGED","oldValue":null,"newValue":["value1
","value2"]},{"key":"id","status":"CHANGED","oldValue":45,"newValue":null},{"key":"key1","status":"REMOVED","oldValue":"value1","newValue":null},{"key":"key2","status":"ADDED","oldValue":null,"newValue":"value2"},{"key":"numbers1","status":"UNCHANGED","oldValue":[1,2,3,4],"newValue":[1,2,3,4]},{"key":"numbe
rs2","status":"CHANGED","oldValue":[2,3,4,5],"newValue":[22,33,44,55]},{"key":"numbers3","status":"REMOVED","oldValue":[3,4,5],"newValue":null},{"key":"numbers4","status":"ADDED","oldValue":null,"newValue":[4,5,6]},{"key":"obj1","status":"ADDED","oldValue":null,"newValue":{"nestedKey":"value","isNested":true}},{"key":"setting1","status":"CHANGED","oldValue":"Some value","newValue":"Another value"},{"key":"setting2","status":"CHANGED","oldValue":200,"newValue":300},{"key":"setting3","status":"CHANGED","oldValue":true,"newValue":"none"}]

```