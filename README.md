# Adam Smasher Armor (Forge 1.20.1)

Броня Адама Смэшера из Cyberpunk 2077 для Minecraft 1.20.1 (Forge 47.x).

- Скин Смэшера + увеличение модели x1.5
- Бонусы полного сета
- Рывок на клавишу **C**

Нужен и на клиенте, и на сервере.

## Требования

- Minecraft **1.20.1**
- Forge **47.x**
- JDK **17** для сборки

## Сборка

```bash
./gradlew build
```

Готовый jar: `build/libs/adamsmasher-1.0.0.jar`  
Положи его в папку `mods` клиента и сервера.

Тест в IDE:

```bash
./gradlew runClient
```

## Крафт

Рецепты есть в `data/adamsmasher/recipes/`.  
Броня появляется во вкладке **Бой** (Combat) креатива.

## Управление

| Клавиша | Действие |
|---------|----------|
| **C**   | Рывок (при полном сете) |

## Структура

```
src/main/java/com/smasher/adamsmasher/
  SmasherMod.java          — регистрация брони
  SmasherArmorItem.java
  SmasherArmorMaterial.java
  SmasherSet.java
  SetBonusEvents.java      — бонусы сета
  DashPacket.java          — сетевой пакет рывка
  ModNetwork.java
  ClientForgeBus.java
  ClientModBus.java
  SmasherPlayerRenderer.java
```

## Лицензия

All Rights Reserved
