# 🗃️ Persistence

[![](https://jitpack.io/v/community.leaf/persistence.svg)](https://jitpack.io/#community.leaf/persistence "Get maven artifacts on JitPack")
[![](https://img.shields.io/badge/License-MPL--2.0-blue)](./LICENSE "Project License: MPL-2.0")
[![](https://img.shields.io/badge/Java-11-orange)](# "Java Version: 11")
[![](https://img.shields.io/badge/View-Javadoc-%234D7A97)](https://javadoc.jitpack.io/community/leaf/persistence/persistence-parent/latest/javadoc/ "View Javadoc")

PersistentDataContainer utilities.

## Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>community.leaf.persistence</groupId>
    <artifactId><!--module--><</artifactId>
    <version><!--release--></version>
</dependency>
```

### Modules

- **`persistence-utilities`** → 🧰
    - Various utilities for PersistentDataContainers, including
      more persistent data types.
- **`json-persistent-data-container`** → 🗄️
    - PersistentDataContainer implementation for JSON (using Gson).

### Versions

Since we use JitPack to distribute this library, the versions available 
are the same as the `tags` found on the **releases page** of this repository.

### Shading

If you intend to shade this library, please consider **relocating** the packages
to avoid potential conflicts with other projects. This library also utilizes
nullness annotations, which may be undesirable in a shaded uber-jar. They can
safely be excluded, and you are encouraged to do so.

