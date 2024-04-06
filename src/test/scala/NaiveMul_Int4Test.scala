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

class NaiveMul_Int4Test extends AnyFlatSpec with ChiselScalatestTester {
  import TestArgs._

  behavior of "NaiveMul_int4"
  it should "NaiveMul_int4 Operations" in {
    test(new NaiveMultiplier_Int4(precision + 1, pipeAt = Seq(1))).withAnnotations(Seq(WriteVcdAnnotation)) { d =>
      d.io.regEnables(0).poke(true.B)

      d.io.a.poke(1.U) //
      d.io.b.poke(2.U)
      d.io.c.poke(1.U)
      d.clock.step(1) // 乘法在第一个时钟周期完成
      // 验证输出结果
      d.io.result_INT4_out_ac.expect(1.U(8.W))
      d.io.result_INT4_out_bc.expect(2.U(8.W))

      d.io.a.poke(1.U) //
      d.io.b.poke(12.U)
      d.io.c.poke(15.U)
      d.clock.step(1) // 乘法在第一个时钟周期完成
      // 验证输出结果
      d.io.result_INT4_out_ac.expect(15.U(8.W))
      d.io.result_INT4_out_bc.expect(180.U(8.W))
    }
  }
}