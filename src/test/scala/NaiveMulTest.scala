package FPUv2

import FPUv2.utils.{TestFPUCtrl, FPUInput}
import FPUv2.utils.FPUOps._
import FPUv2.utils.RoundingModes._
import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import Parameters._

class NaiveMulTest extends AnyFlatSpec with ChiselScalatestTester {
  import TestArgs._

  behavior of "NaiveMul"
  it should "NaiveMul Operations" in {
    test(new NaiveMultiplier(precision + 1, pipeAt = Seq(1))).withAnnotations(Seq(WriteVcdAnnotation)) { d =>
      d.io.regEnables(0).poke(true.B)

//      d.io.a.poke("hAAA".asUInt()) // 16进制数 10 (AAAA)
//      d.io.b.poke("h001".asUInt()) // 16进制数 1

      d.io.a.poke("b000000010000".asUInt()) // 16进制数 10 (AAAA)
      d.io.b.poke("b000000100000".asUInt()) // 16进制数 1
      // 执行测试
      d.clock.step(1) // 乘法在第一个时钟周期完成

      // 验证输出结果
//      d.io.result.expect("hAAA".U((2 * (precision + 1)).W))
      d.io.result.expect("b000000000000001000000000".U((2 * (precision + 1)).W))

    }
  }
}