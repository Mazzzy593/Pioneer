### 创新性说明

#### 短暂连接传输的蓝牙数据交互方式

1、payload 长度的要求

蓝牙广播包的方式限制了 payload 的长度范围，使得传输数据的长度受到限制。

对于一些 Receive Only 的设备，其它设备无法读取其 payload，此时通过数据的读写来实现传输，并在内部实现了对分片传播的处理，使得最大的可传输数据长度变为 512 字节。因此需要通过建立短暂蓝牙连接的方式写入数据。

2、广播包的不安全性

广播包的时候其他的任意设备都可以收到发送出去的 payload，因此处于安全性考虑，不采用这种方式发送数据包。

3、蓝牙连接提高安全性

通过蓝牙之间建立短暂连接的方式去交互，可以确保收到 payload的设备必然安装本 Pioneer APP，此时发送数据包提高安全性。

4、多设备交互

通过短暂连接传输交互数据一次同时连接多个设备，连接效果效果稳定。

#### 抗重放攻击 MAC

在传统的 payload 的基础上添加了有效期的限制和 MAC 的认证，从而提高了系统对于重放攻击的防御能力。

1、有效期验证

在收到数据包的时候，先去检测收到时间是否在数据包有效期之内，如果在有效期之内则收下，否则丢弃数据包。

2、MAC 验证

MAC 是在 APP 下载了感染者信息，与本地保存的临时标识符去匹配。MAC 是由 payload 的四项和当天的 Matching Key 通过 HMAC-SHA256生成的。

​	在匹配成功的情况下，则去验证 MAC，验证通过方可说明该用户是密切接触者，则后台自动上传该用户近 14 天的 Matching Keys。否则，认为该数据包是被伪造的。

![image-20241230155115449](https://cdn.jsdelivr.net/gh/lunan0320/Pioneer@main/images/202412301551501.png)

#### 去中心化模式

​	采用去中心化的方式，最大程度的保护了用户的隐私，同时又保证了中心服务器对于感染者和密切接触者的精确追踪。

1、中心化模式

传统的追踪系统是把数据上传到中心服务器，由中心服务器保存信息，并直接在服务器端计算匹配。

2、去中心化模式

本系统采用去中心化的方式，用户将接收到的临时身份标识符保存到本地，将计算匹配接触者信息的工作放到手机本地。

​	在该用户没有接触感染者的情况下，中心服务器不会知道关于该用户的任何的接触信息。即使该用户称为了密切接触者，别人也无法获悉该用户的任何隐私信息，也就无法知道该用户的身份。

#### 密码学技术

1、临时信息和密钥的 Hash 链式生成

​	采取以 SM3 算法的方式，以 Secret key 生成 Matching keys 以及生成 contanctkey，由 Secret key 生成 Contact identifier 的过程中涉及到了伪随机数生成器 (PRNG) 和伪随机函数 (PRNF)，前者用于生成伪随机数，后者用于将分布可能不均匀的输入空间映射到分布均匀的输出空间。

![image-20241230155342364](https://cdn.jsdelivr.net/gh/lunan0320/Pioneer@main/images/202412301553445.png)

简要流程如下

![image-20241230155427576](https://cdn.jsdelivr.net/gh/lunan0320/Pioneer@main/images/202412301554601.png)

详细流程如下：

在用户第一次打开 app 的时候，app 内部会自动调用伪随机数生生成器（PRNG）生成一个 Secret key 与用户的手机号进行绑定，并提交到服务器：

![image-20241230155457646](https://cdn.jsdelivr.net/gh/lunan0320/Pioneer@main/images/202412301554664.png)

接下来由 Secret key 可以生成一系列的 matching seeds。若打算提交生成 n 个 Contact identifier，则 Matching seeds 由以下递推式生成：

![image-20241230155550386](https://cdn.jsdelivr.net/gh/lunan0320/Pioneer@main/images/202412301555413.png)

此处 Truncate 函数表示截取输入一半的长度。



​	matching seeds 用于生成 matching keys，生成关系式如下：

![image-20241230155645727](https://cdn.jsdelivr.net/gh/lunan0320/Pioneer@main/images/202412301556758.png)

Maching key 用于生成每一天的 Contact key，Contact key 从Matching key 的生成过程，与 Maching key 从 Secret key 生成的过程完全相同。



最后，我们得到了一系列 Contact key，Contact key 用于生成Contact identifier，Contact identifier 是最终用于蓝牙数据交互的临时用户标识符：

![image-20241230155710233](https://cdn.jsdelivr.net/gh/lunan0320/Pioneer@main/images/202412301557256.png)