package FPUv2

import FPUv2.utils.{TestFPUCtrl, FPUInput}
import FPUv2.utils.FPUOps._
import FPUv2.utils.RoundingModes._
import FPUv2.{FMULReused}

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import Parameters._

class FMULReusedTest extends AnyFlatSpec with ChiselScalatestTester {
  import TestArgs._
  object subModuleInput { d: FPUInput =>
    var count = 0
    def reset = { count = 0 }
    def apply(a: AnyVal, b: AnyVal, c: AnyVal, op: UInt, rm: UInt = RTZ) = {
      count = (count + 1) % len
      (new FPUInput(len, new TestFPUCtrl(depthWarp, softThread))).Lit(
        _.a -> toUInt(a).U,
        _.b -> toUInt(b).U,
        _.c -> toUInt(c).U,
        _.op -> op(2,0),
        _.rm -> rm,
        _.ctrl.get -> (new TestFPUCtrl(depthWarp, softThread)).Lit(
          _.regIndex -> count.U,
          _.vecMask -> 0.U,
          _.warpID-> 0.U,
          _.wvd -> false.B,
          _.wxd -> false.B
        )
      )
    }
  }
  object subModuleInputInt8 { d: FPUInput =>
    var count = 0
    def reset = { count = 0 }
    def apply(a: AnyVal, b: AnyVal, c: AnyVal, op: UInt, rm: UInt = RTZ) = {
      count = (count + 1) % len
      (new FPUInput(len, new TestFPUCtrl(depthWarp, softThread))).Lit(
        _.a -> "b0000000000010000".U,
        _.b -> "b0000000000100000".U(16.W),
        _.c -> toUInt(c).U,
        _.op -> op(2,0),
        _.rm -> rm,
        _.ctrl.get -> (new TestFPUCtrl(depthWarp, softThread)).Lit(
          _.regIndex -> count.U,
          _.vecMask -> 0.U,
          _.warpID-> 0.U,
          _.wvd -> false.B,
          _.wxd -> false.B
        )
      )
    }
  }
  object subModuleInputInt4 { d: FPUInput =>
    var count = 0
    def reset = { count = 0 }
    def apply(a: AnyVal, b: AnyVal, c: AnyVal, op: UInt, rm: UInt = RTZ) = {
      count = (count + 1) % len
      (new FPUInput(len, new TestFPUCtrl(depthWarp, softThread))).Lit(
        _.a -> toUInt(a).U,
        _.b -> toUInt(b).U,
        _.c -> toUInt(c).U,
        _.op -> op(2,0),
        _.rm -> rm,
        _.ctrl.get -> (new TestFPUCtrl(depthWarp, softThread)).Lit(
          _.regIndex -> count.U,
          _.vecMask -> 0.U,
          _.warpID-> 0.U,
          _.wvd -> false.B,
          _.wxd -> false.B
        )
      )
    }
  }

  behavior of "FMULReused"
  it should "FMULReused Operations" in {
    test(new FMULReused(expWidth, precision, new TestFPUCtrl(depthWarp, softThread))).withAnnotations(Seq(WriteVcdAnnotation)) { d =>
      subModuleInput.reset
      d.io.in.initSource()
      d.io.in.setSourceClock(d.clock)
      d.io.out.initSink()
      d.io.out.setSinkClock(d.clock)
      d.clock.setTimeout(20)
      d.io.out.ready.poke(true.B)
      fork{
        d.io.in.enqueueSeq(Seq(
//          subModuleInput(1.0f, 10.0f, 1.0f, FN_FMADD),
//          subModuleInput(2.0f, 10.0f, 2.0f, FN_FMADD),
          subModuleInput(1.0f, 10.0f, 0.0f, FN_FMUL),
          subModuleInput(4.0f, 10.0f, 0.0f, FN_FMUL)
//          subModuleInput(5.0f, 10.0f, 0, FN_FMADD),
//          subModuleInput(6.0f, 0.0f, 6.0f, FN_FADD),
//          subModuleInput(7.0f, 0.0f, 6.0f, FN_FADD)//,
//          subModuleInput(77.0f, 0, 0, FN_F2I),
//          subModuleInput(88, 0, 0, FN_I2F)
        ))
      }.fork {
        d.io.out.ready.poke(true.B)
      }.join()

//      d.INTmode.poke(true.B)
//      subModuleInputInt8(1.0f, 2.0f, 0.0f, FN_FMUL)
//      d.clock.step(4)
//      d.multiplierResult.expect("b00000000001000000000".asUInt(24.W))
//      d.io.out.bits.result.expect(10.U)
      d.clock.step(10)
    }
  }
}
