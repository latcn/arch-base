# Arch-Base 架构底座

**一个克制、务实、可插拔的 Java 架构基础设施集合**

[![JDK](https://img.shields.io/badge/JDK-17+-green.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1+-green.svg)](https://spring.io/projects/spring-boot)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.4+-blue.svg)](https://baomidou.com/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

---

## 📖 目录

- [项目简介](#项目简介)
- [核心设计理念](#核心设计理念)
- [模块说明](#模块说明)
- [快速开始](#快速开始)
- [核心功能详解](#核心功能详解)
- [CQRS 模块说明](#cqrs-模块说明)
- [配置说明](#配置说明)
- [与 Spring 生态的关系](#与-spring-生态的关系)
- [架构决策记录](#架构决策记录)
- [依赖](#依赖)
- [许可证](#许可证)

---

## 项目简介

**Arch-Base** 是一个轻量级、非侵入式的 Java 架构基础设施集合。它不试图取代 Spring 生态，而是在其基础上提供**可选的、开箱即用的架构规范和扩展点**，帮助团队在 DDD（领域驱动设计）、CQRS（命令查询职责分离）和复杂业务系统中保持代码的整洁和架构的一致性。

### 为什么需要 Arch-Base？

在 Spring Boot 项目中，我们常常面临以下问题：

- DDD 的 `Entity`、`ValueObject`、`Repository` 等概念没有统一的基类，每个项目重复造轮子
- 查询条件（分页、排序、动态过滤）缺乏标准化的传递和解析方式
- CQRS 架构中的 Command/Query 缺乏统一的路由和拦截规范
- 从单体演进到微服务时，业务代码与基础设施耦合过重
- 事件溯源（Event Sourcing）中，领域事件的捕获和发布依赖开发者手动编码

**Arch-Base 不是要替代 Spring**，而是提供一套**可选的、渐进式的**架构组件，让你在需要时引入，在不需要时完全不受影响。

### 项目特点

| 特点 | 说明 |
| :--- | :--- |
| **按需取用** | 所有模块默认关闭，通过配置显式开启，绝不绑架你的项目 |
| **克制务实** | 不为了架构而架构，每一个功能都来自真实的工程痛点 |
| **非侵入式** | 不强制继承特定基类，不修改字节码，不依赖特殊类加载器 |
| **与 Spring 共生** | 不试图替代 Spring 的任何功能，而是在其基础上提供额外的架构规范 |
| **类型安全** | 充分利用 Java 泛型和 MyBatis-Plus 的 `TableInfoHelper`，在编译期和运行时保障安全 |

---

## 核心设计理念

### 1. 按需取用（Opt-in）

所有功能模块默认关闭，必须通过配置显式开启：

```yaml
archbase:
  enabled: true               # 全局开关
  cqrs:
    enabled: true             # 开启 CQRS 总线
  mybatis:
    enabled: true             # 开启 MyBatis-Plus 增强
  web:
    enabled: true             # 开启 Web 统一处理
```

### 2. 克制务实

**每一个功能都必须回答一个问题**："这个问题 Spring 已经解决了吗？如果已经解决了，我们为什么还需要这个？"

- 如果 Spring 已经有了完善的解决方案（如 `@Cacheable`、`@Retryable`、`@TransactionalEventListener`），Arch-Base **绝不重复造轮子**
- Arch-Base 只解决 Spring 生态中**尚未标准化**或**需要额外规范约束**的领域

### 3. 与 Spring 共生

| 能力 | Spring 原生 | Arch-Base 补充 |
| :--- | :--- | :--- |
| 依赖注入 | ✅ `@Autowired` / `@Component` | — |
| AOP 拦截 | ✅ `@Around` / `@Before` | — |
| 缓存 | ✅ `@Cacheable` / `@CacheEvict` | — |
| 重试 | ✅ `@Retryable` | — |
| 异步处理 | ✅ `@Async` | — |
| 事务事件 | ✅ `@TransactionalEventListener` | — |
| **DDD 基类** | ❌ 无标准化方案 | ✅ `BaseEntity`、`IValueObject`、`IRepository` |
| **动态查询条件树** | ❌ 无标准化方案 | ✅ `PageQuery` + `ConditionParser` |
| **CQRS 路由总线** | ⚠️ 可自行 AOP 实现 | ✅ 标准化的 `ICommandBus` / `IQueryBus` + 拦截器 |
| **事件溯源自动捕获** | ⚠️ 需手动发布事件 | ✅ Bus 自动捕获并存储领域事件 |

---

## 模块说明

```
arch-base (父工程)
├── archbase-bom                 # Maven BOM（统一依赖版本管理）
│
├── archbase-core                # 核心契约层（零依赖，纯 JDK）
│   ├── ICommand / IQuery / IResponse  # CQRS 标记接口
│   ├── BaseException + IErrorCode      # 异常体系
│   └── CustomExceptionHandler          # 异常处理 SPI
│
├── archbase-foundation          # DDD 地基层（依赖 core）
│   ├── BaseEntity<ID>                  # DDD 实体基类（仅含 ID）
│   ├── IValueObject                    # 值对象标记接口
│   ├── BaseDomainEvent                 # 领域事件基类
│   ├── IRepository<Entity, ID>         # 仓储契约
│   ├── IRepositoryFactory              # 仓储工厂 SPI
│   ├── IAssembler<Entity, DO>          # 实体转换器接口
│   ├── PageQuery / PageResult          # 分页模型
│   ├── SortItem / SortDirection        # 排序模型
│   └── Condition / LogicGroup / FilterCondition  # 条件树模型
│
├── archbase-data-mybatis         # MyBatis-Plus 适配器（可选）
│   ├── AbstractBaseRepository          # 仓储基类（含分页、排序、条件解析）
│   ├── ConditionParser                 # 条件树 → QueryWrapper 解析器
│   ├── MybatisRepositoryFactory        # 仓储工厂实现
│   └── AutoFillMetaObjectHandler       # 审计字段自动填充
│
├── archbase-web-spring           # Spring Web 适配器（可选）
│   ├── Result<T>                       # 统一响应体
│   ├── GlobalExceptionHandler          # 全局异常处理基类
│   ├── DefaultExceptionHandler         # 默认异常处理器实现
│   ├── RequestParamResolver            # 参数解析器
│   └── RequestContext                  # 请求上下文（traceId、userId、tenantId）
│
├── archbase-cqrs                 # CQRS 引擎（可选）
│   ├── ICommandBus / IQueryBus         # 命令/查询总线接口
│   ├── ICommandHandler / IQueryHandler # 处理器接口
│   ├── IBusInterceptor                 # 总线拦截器 SPI
│   ├── InMemoryCommandBus              # 内存总线默认实现
│   ├── InMemoryQueryBus                # 内存查询总线默认实现
│   ├── HandlerRegistry                 # 处理器自动注册器
│   └── BaseController                  # CQRS Controller 基类（减少胶水代码）
│
├── archbase-starter              # Spring Boot Starter（推荐）
│   └── 条件装配，按需开启各模块
│
└── archbase-example              # 示例项目
    └── 展示各模块的组合使用方式
```

### 模块依赖关系

```text
用户应用 (User Project)
    │
    ├─► archbase-bom (可选)            # 统一版本管理
    │
    └─► archbase-starter (可选)        # 条件装配
            │
            ├─► archbase-web-spring (可选)   ──► archbase-core
            ├─► archbase-data-mybatis (可选) ──► archbase-foundation ──► archbase-core
            └─► archbase-cqrs (可选)         ──► archbase-core
```

> **核心原则**：`archbase-core` 零依赖，`archbase-foundation` 只依赖 `core`，其他模块按需引入。

---

## 快速开始

### Maven 依赖

```xml
<!-- 方式一：使用 BOM 统一管理版本（推荐） -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.latcn.archbase</groupId>
            <artifactId>archbase-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 引入 Starter（推荐） -->
<dependency>
    <groupId>io.github.latcn.archbase</groupId>
    <artifactId>archbase-starter</artifactId>
</dependency>

<!-- 方式二：直接引入模块（需指定版本） -->
<dependency>
    <groupId>io.github.latcn.archbase</groupId>
    <artifactId>archbase-foundation</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>io.github.latcn.archbase</groupId>
    <artifactId>archbase-data-mybatis</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 配置启用

```yaml
# application.yml
archbase:
  enabled: true
  mybatis:
    enabled: true          # 启用 MyBatis-Plus 增强
  web:
    enabled: true          # 启用 Web 统一处理
  cqrs:
    enabled: true          # 启用 CQRS 总线（可选）
```

### 定义实体和仓储

```java
// 1. 定义 DO（MyBatis-Plus 映射类）
@TableName("t_order")
public class OrderDO {
    @TableId
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime createTime;
}

// 2. 定义领域实体
public class Order extends BaseEntity<Long> {
    private Long userId;
    private BigDecimal amount;
    private OrderStatus status;
    
    public static Order create(Long userId, BigDecimal amount) {
        Order order = new Order();
        order.userId = userId;
        order.amount = amount;
        order.status = OrderStatus.PENDING;
        return order;
    }
}

// 3. 定义仓储接口
public interface IOrderRepository extends IRepository<Order, Long> {
    PageResult<Order> dynamicQuery(PageQuery pageQuery);
}

// 4. 实现仓储
@Repository
public class OrderRepositoryImpl 
        extends AbstractBaseRepository<Order, OrderDO, Long> 
        implements IOrderRepository {
    
    @Override
    public PageResult<Order> dynamicQuery(PageQuery pageQuery) {
        return dynamicPageQuery(pageQuery);
    }
}
```

### 使用动态查询

```java
// Controller
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderRepository orderRepository;

    @PostMapping("/page")
    public Result<PageResult<OrderVO>> page(@RequestBody PageQuery pageQuery) {
        PageResult<Order> result = orderRepository.dynamicQuery(pageQuery);
        return Result.success(result.map(OrderVO::from));
    }
}

// 前端传参示例
{
  "pageNum": 1,
  "pageSize": 20,
  "sorts": [{"field": "createTime", "direction": "DESC"}],
  "condition": {
    "logic": "AND",
    "conditions": [
      {"field": "userId", "operator": "EQ", "value": 1001},
      {"field": "amount", "operator": "GT", "value": 100}
    ]
  }
}
// 生成 SQL: WHERE user_id = 1001 AND amount > 100 ORDER BY create_time DESC
```

---

## 核心功能详解

### 1. 分页与动态查询（`PageQuery` + 条件树）

#### 分页模型 `PageQuery`

`PageQuery` 集成了分页、排序和条件树，是统一的查询请求对象：

```java
PageQuery query = new PageQuery();
query.setPageNum(1);
query.setPageSize(20);
query.setSorts(List.of(SortItem.desc("createTime"), SortItem.asc("id")));
```

#### 条件树（支持任意 AND/OR 嵌套）

```java
// 构建条件： (userId = 1001 OR status = 2) AND amount > 100
Condition condition = LogicGroup.and(
    LogicGroup.or(
        FilterCondition.of("userId", Operator.EQ, 1001),
        FilterCondition.of("status", Operator.EQ, 2)
    ),
    FilterCondition.of("amount", Operator.GT, 100)
);

PageQuery pageQuery = new PageQuery();
pageQuery.setPageNum(1);
pageQuery.setPageSize(20);
pageQuery.setCondition(condition);
```

#### 支持的查询操作符

| 操作符 | 说明 | SQL 示例 |
| :--- | :--- | :--- |
| `EQ` | 等于 | `column = ?` |
| `NE` | 不等于 | `column <> ?` |
| `GT` | 大于 | `column > ?` |
| `GE` | 大于等于 | `column >= ?` |
| `LT` | 小于 | `column < ?` |
| `LE` | 小于等于 | `column <= ?` |
| `LIKE` | 模糊匹配 | `column LIKE '%?%'` |
| `LIKE_LEFT` | 左匹配 | `column LIKE '?%'` |
| `LIKE_RIGHT` | 右匹配 | `column LIKE '%?'` |
| `IN` | 包含 | `column IN (?, ?, ...)` |
| `NOT_IN` | 不包含 | `column NOT IN (?, ?, ...)` |
| `BETWEEN` | 区间 | `column BETWEEN ? AND ?` |
| `IS_NULL` | 为空 | `column IS NULL` |
| `IS_NOT_NULL` | 不为空 | `column IS NOT NULL` |

### 2. DDD 基类

#### `BaseEntity<ID>`

仅包含 `id` 字段和基于 ID 的 `equals/hashCode`，**不包含**审计字段（`createTime`、`updateTime`）和乐观锁字段（`version`）。

```java
public class Order extends BaseEntity<Long> {
    // 只关心业务字段，技术字段由 MyBatis-Plus 自动填充
}
```

#### `IValueObject`

值对象标记接口，用于标识不可变的、通过属性值判断相等性的对象。

#### `BaseDomainEvent`

领域事件基类，包含 `eventId`、`occurredAt`、`sourceId`。

### 3. MyBatis-Plus 增强

#### `AbstractBaseRepository`

提供开箱即用的 CRUD 和分页查询能力：

```java
// 子类无需编写任何 CRUD 代码
@Repository
public class OrderRepositoryImpl 
        extends AbstractBaseRepository<Order, OrderDO, Long> 
        implements IOrderRepository {
    // 只需实现自定义查询方法
}
```

#### `ConditionParser`

将条件树安全地转换为 MyBatis-Plus `QueryWrapper`，支持字段白名单校验（不存在的字段自动忽略）：

```java
QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
ConditionParser.applyCondition(wrapper, pageQuery.getCondition(), OrderDO.class);
```

#### `AutoFillMetaObjectHandler`

自动填充审计字段（`createTime`、`updateTime`、`isDeleted`），无需在业务代码中手动设置。

### 4. 实体转换器 `IAssembler`

定义领域实体与持久化对象之间的转换契约，推荐配合 MapStruct 使用：

```java
@Mapper(componentModel = "spring")
public interface OrderAssembler extends IAssembler<Order, OrderPO> {
    // 自动生成实现
}
```

### 5. 异常体系

```java
// 业务方定义错误码枚举
public enum BizErrorCode implements IErrorCode {
    ORDER_NOT_FOUND("B001", "订单不存在"),
    INSUFFICIENT_BALANCE("B002", "余额不足");
}

// 抛出异常（链式设置上下文）
throw BaseException.of(BizErrorCode.ORDER_NOT_FOUND)
    .set("orderId", orderId)
    .set("userId", userId);
```

---

## CQRS 模块说明

### 定位

`archbase-cqrs` 是一个**可选的架构规范工具**，而非“不可替代的技术组件”。它的价值在于：

1. **统一写操作入口**：所有 Command 走 `ICommandBus`，便于架构治理和代码审查
2. **框架级扩展点**：为未来的架构演进（如引入事件溯源）预留统一入口
3. **事件溯源自动化**：在事件溯源架构中自动捕获和存储领域事件，减少人为遗漏

### 与 Spring 的关系

| 能力 | Spring 原生 | Arch-Base CQRS |
| :--- | :--- | :--- |
| 本地 Command 执行 | `@Service` 直接调用 | `ICommandBus` + `ICommandHandler` |
| 统一拦截 | AOP `@Around` | `IBusInterceptor` |
| 事务后异步事件发布 | `@TransactionalEventListener` + `@Async` | Bus 自动捕获（事件溯源场景） |
| 透明切换本地/远程 | 需自行抽象 | `ICommandBus` 接口允许替换实现 |

### 使用示例

```java
// 1. 定义 Command
public class CreateOrderCommand implements ICommand<OrderIdResponse> {
    private Long userId;
    private List<OrderItemDTO> items;
}

// 2. 定义 Handler
@Component
public class CreateOrderHandler 
        implements ICommandHandler<CreateOrderCommand, OrderIdResponse> {
    @Override
    public OrderIdResponse handle(CreateOrderCommand cmd) {
        Order order = Order.create(cmd.getUserId(), cmd.getItems());
        orderRepository.save(order);
        return new OrderIdResponse(order.getId());
    }
}

// 3. Controller 使用
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {
    @PostMapping("/create")
    public Result<OrderIdResponse> create(@RequestBody CreateOrderCommand cmd) {
        return exec(cmd);  // 继承自 BaseController
    }
}
```

### 何时使用 CQRS 模块？

| 场景 | 是否建议使用 |
| :--- | :--- |
| 简单的 CRUD 应用 | ❌ 不需要，直接用 `@Service` |
| 需要统一写操作入口的复杂系统 | ✅ 建议使用 |
| 计划从单体演进为微服务 | ✅ 建议使用（Bus 接口方便后续切换） |
| 采用事件溯源（Event Sourcing） | ✅ 建议使用（Bus 自动捕获事件） |
| 需要灰度路由或动态策略 | ⚠️ 可选用，但 Handler 内部策略模式也可实现 |

### 关于 `IBusInterceptor` 与 Spring AOP 的关系

`IBusInterceptor` 和 Spring AOP 是**互补**而非替代关系：

| 切面类型 | 推荐方式 |
| :--- | :--- |
| 通用技术切面（日志、性能监控） | Spring AOP `@Around` |
| 通用技术切面（缓存、重试、降级） | Spring 注解 `@Cacheable` / `@Retryable` |
| 领域相关切面（基于 Command 状态的路由） | `IBusInterceptor` |
| 事件溯源事件自动捕获 | `IBusInterceptor` |

---

## 配置说明

### 完整配置示例

```yaml
archbase:
  # 全局开关
  enabled: true
  application-name: my-service

  # CQRS 配置
  cqrs:
    enabled: true
    mode: memory              # memory | kafka | rabbitmq

  # MyBatis-Plus 配置
  mybatis:
    enabled: true
    auto-fill: true           # 自动填充审计字段

  # Web 配置
  web:
    enabled: true
    exception-handler: default
```

### 条件装配原则

所有模块**默认关闭**，必须通过配置显式开启。这遵循了 **"按需取用"** 的核心设计理念，避免引入不必要的依赖。

---

## 与 Spring 生态的关系

### 为什么我们不重复造轮子？

Arch-Base 的定位是 **"补充而非替代"**：

| Spring 已有能力 | Arch-Base 做法 |
| :--- | :--- |
| `@Cacheable` / `@CacheEvict` | ✅ 直接使用，不重复实现 |
| `@Retryable` / Resilience4j | ✅ 直接使用，不重复实现 |
| `@TransactionalEventListener` | ✅ 直接使用，不重复实现 |
| `@Async` | ✅ 直接使用，不重复实现 |
| AOP `@Around` | ✅ 直接使用，不重复实现 |
| **DDD 战术建模基类** | ⚠️ Spring 无标准化方案 → **提供** |
| **动态查询条件树** | ⚠️ Spring 无标准化方案 → **提供** |
| **CQRS 路由总线** | ⚠️ Spring 可自行 AOP 实现 → **提供标准化规范** |

### 设计哲学

**"Spring 能做的，我们绝不重复；Spring 没做的，我们谨慎补充。"**

---

## 架构决策记录（ADR）

### 1. 为什么 `PageQuery` 放在 `foundation` 而不是 `core`？

**决策**：置于 `foundation` 层。

**理由**：分页模型（`pageNum/pageSize`）是技术实现细节，不应污染零依赖的核心契约。Cursor-based 分页、Token 分页等现代分页方案有完全不同的参数结构，锁死 `PageQuery` 在 Core 层会限制扩展。

### 2. 为什么使用接口 + `I` 前缀？

**决策**：所有接口统一加 `I` 前缀（如 `ICommand`、`IRepository`），`CustomExceptionHandler` 作为函数式接口特例不加。

**理由**：符合 Java 社区常见命名习惯，提高代码可读性，便于在 IDE 中快速识别接口类型。

### 3. 为什么 `BaseEntity` 只包含 `id` 字段？

**决策**：`BaseEntity` 仅包含 `id` 和基于 ID 的 `equals/hashCode`，不包含审计字段（`createTime`/`updateTime`）和乐观锁字段（`version`）。

**理由**：
- 审计字段属于基础设施关注点，不应侵入领域层
- 非所有聚合需要乐观锁
- 技术字段通过 MyBatis-Plus 的 `MetaObjectHandler` 自动填充，保持领域层纯净

### 4. 为什么放弃 APT 生成 SFunction 映射？

**决策**：不提供 APT 生成 `SFunction` 映射表的功能。

**理由**：
- 生成的类会被其他类提前引用，导致编译顺序问题
- Lombok 式的 AST 修改对 JDK 版本敏感，维护成本高
- 使用 `QueryWrapper` + `TableInfoHelper` 运行时转换已经足够
- 保持框架简单、稳定，避免引入复杂的编译时依赖

### 5. 为什么默认禁用所有模块？

**决策**：所有模块默认 `enabled=false`，必须显式开启。

**理由**：
- 避免"引入即激活"的隐式副作用
- 严格遵循"按需取用"原则
- 用户只引入自己需要的功能，减少启动时间和类加载开销

---

## 依赖

### 必须依赖

| 依赖 | 版本 | 说明 |
| :--- | :--- | :--- |
| JDK | 17+ | Java 运行环境 |
| MyBatis-Plus | 3.4.0+ | 仅在使用 `data-mybatis` 模块时需要 |

### 可选依赖

| 依赖 | 版本 | 说明 |
| :--- | :--- | :--- |
| Spring Boot | 3.1.x | 使用 `web-spring` 或 `starter` 模块时需要 |
| MyBatis-Plus | 3.4.0+ | 使用 `data-mybatis` 模块时需要 |

> **重要**：`mybatis-plus-boot-starter` 以 `provided` 方式引入，由使用方决定版本，Arch-Base **不强制绑定任何版本**。

---

## 许可证

[Apache License 2.0](LICENSE)

---

## 贡献指南

欢迎提交 Issue 和 Pull Request。在提交之前，请确保：

1. 代码通过所有测试
2. 遵循现有的代码风格
3. 更新相关文档

---

## 总结

**Arch-Base 不是要告诉你"你必须这样写代码"，而是提供一套"当你需要时，可以方便地使用的工具箱"。**

它不试图取代 Spring 的任何功能，而是在 Spring 生态的空白处，提供经过工程验证的、可复用的架构组件。每一个功能都来自真实的工程痛点，每一次设计决策都经过"是否被 Spring 替代"的严格审视。

**保持克制，保持务实，让架构服务于业务，而不是相反。**