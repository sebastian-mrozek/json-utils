Introduction
---
When testing systems that operate using data in JSON format (REST APIs) there is often a need to compare JSON documents (or JSON objects).
Strict comparison is not always the best approach:
- test data might not be interested in every single field in the actual data
- actual data might include values that cannot be used for comparison, i.e.: ids, timestamps, etc.

Common solutions to this problem are:
- stripping objects off ignored fields
- masking ignored fields values with an ignored constant

Those solutions don't always work well, specially for complex object graphs.

`JsonUtils` allows to express a 'contains' logic on JSON objects, in other words it allows testing whether an actual object contains all fields from the expected object.
The most complex part of that logic is expressing what it means that an array of potentially complex objects contains another array.
The solution works for arrays regardless of element ordering.

The utility also lists what fields or which array elements (using their indexes) were not found.

Scenarios
---
Check the unit tests for all scenarios covered, below is just a few examples.

*Simple object comparison*

Actual object (ao1):
```
{
  "id": "f7db1ab0-f600-4ea7-9562-c346c4dcc3f3",
  "name": "Tym",
  "age": 37,
  "updatedMillis": 1349333576093
}
```

Expected object used to assert the data in the test, passing scenario (eo1):
```
{
  "name": "Tym",
  "age": 37
}
```

Failing scenario (eo2):
```
{
  "name": "Tym",
  "age": 38
  "surname": "Dude"
}
```

`JsonUtils.assertContains(ao1, eo1)` passes.

`JsonUtils.assertContains(ao1, eo2)` fails due to `age` field value being different and a missing field `surname`.

*Simple array comparison*

Actual array (aa3):
```
[
  {
    "id": "tyu",
    "c": 3
  },
  {
    "id": "zxy",
    "a": 1
  },
  {
    "id": "123",
    "b": 2
  }
]
```

Expected array, passing scenario (ea4):
```
[
  {
    "a": 1
  },
  {
    "b": 2
  },
  {
    "c": 3
  }
]
```

Expected array, failing scenario (ea5):
```
[
  {
    "a": 1
  },
  {
    "a": 1
  },
  {
    "c": 3
  }
]
```

`JsonUtils.assertContains(aa3, ea4)` passes, all the elements in ea4 are matching elements in aa3.

`JsonUtils.assertContains(aa3, ea5)` fails, `{"a": 1}` object matches only one element in the actual array, but is expected to be present twice.

Dependencies
---
- [Jackson](https://github.com/FasterXML/jackson)
- [AssertJ](https://assertj.github.io/doc/)
