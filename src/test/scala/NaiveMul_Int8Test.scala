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

class NaiveMul_Int8Test extends AnyFlatSpec with ChiselScalatestTester {
  import TestArgs._

  behavior of "NaiveMul_Int8"
  it should "NaiveMul Operations" in {
    test(new NaiveMultiplier_Int8(12, pipeAt = Seq(1))).withAnnotations(Seq(WriteVcdAnnotation)) { d =>
      d.io.regEnables(0).poke(true.B)

      d.io.a.poke(1.U) // 16进制数 10 (AAAA)
      d.io.b.poke(2.U) // 16进制数 1
      d.clock.step(1) // 乘法在第一个时钟周期完成
      // 验证输出结果
      d.io.result_16.expect(2.U(16.W))

      d.io.a.poke(255.U) // 16进制数 10 (AAAA)
      d.io.b.poke(1.U) // 16进制数 1
      d.clock.step(1) // 乘法在第一个时钟周期完成
      // 验证输出结果
      d.io.result_16.expect(255.U(16.W))
    }
  }
}