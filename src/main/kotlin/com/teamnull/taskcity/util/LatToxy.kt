package com.teamnull.taskcity.util

object LatToxy {
    fun convert( lon: Double, lat: Double ): Point  {
        val ok = 0.9996
        val fe = 500000.0
        val a = 6378137.0;
        val f = 1.0 / 298.257223563
        if( lon < 13.5 ||  lon > 25.5 ){
            return Point(-1.0, -1.0)
            //wsp spoza zakresu PL-2000
        }
        val nfn = 0;


        var olam = 0.0
        var strf = 0.0
        if( lon >= 19.5 && lon < 22.5 ){
        //strefa 7
            olam = Math.toRadians(21.0)
            strf = 7000000.0
        }else {
            System.out.println("nie krakowskie wsp")
            return Point(-1.0, -1.0);
            //throw "no cracow coordinates!"
        }
        val latRad = Math.toRadians(lat)
        val lonRad = Math.toRadians(lon)

        val recf = 1.0 / f
        val b = a * (recf - 1.0) / recf
        val eSquared = CalculateESquared (a, b);
        val e2Squared = CalculateE2Squared (a, b);
        val tn = (a - b) / (a + b);
        val ap = a * (1.0 - tn + 5.0 * ((tn * tn) - (tn * tn * tn)) / 4.0 + 81.0 * ((tn * tn * tn * tn) - (tn * tn * tn * tn * tn)) / 64.0);
        val bp = 3.0 * a * (tn - (tn * tn) + 7.0 * ((tn * tn * tn) - (tn * tn * tn * tn)) / 8.0 + 55.0 * (tn * tn * tn * tn * tn) / 64.0) / 2.0;
        val cp = 15.0 * a * ((tn * tn) - (tn * tn * tn) + 3.0 * ((tn * tn * tn * tn) - (tn * tn * tn * tn * tn)) / 4.0) / 16.0;
        val dp = 35.0 * a * ((tn * tn * tn) - (tn * tn * tn * tn) + 11.0 * (tn * tn * tn * tn * tn) / 16.0) / 48.0;
        val ep = 315.0 * a * ((tn * tn * tn * tn) - (tn * tn * tn * tn * tn)) / 512.0;
        val dlam = lonRad - olam;
        val s = Math.sin (latRad);
        val c = Math.cos (latRad);
        val t = s / c;
        val eta = e2Squared * (c * c);
        val sn = sphsn (a, eSquared, latRad);
        val tmd = sphtmd (ap, bp, cp, dp, ep, latRad);
        val t1 = tmd * ok;
        val t2 = sn * s * c * ok / 2.0;
        val t3 = sn * s * (c * c * c) * ok * (5.0 - (t * t) + 9.0 * eta + 4.0 * (eta * eta)) / 24.0;
        val t4 = sn * s * (c * c * c * c * c) * ok * (61.0 - 58.0 * (t * t) + (t * t * t * t) + 270.0 * eta - 330.0 * (t * t) * eta + 445.0 * (eta * eta) + 324.0 * (eta * eta * eta) - 680.0 * (t * t) * (eta * eta) + 88.0 * (eta * eta * eta * eta) - 600.0 * (t * t) * (eta * eta * eta) - 192.0 * (t * t) * (eta * eta * eta * eta)) / 720.0;
        val t5 = sn * s * (c * c * c * c * c * c * c) * ok * (1385.0 - 3111.0 * (t * t) + 543.0 * (t * t * t * t) - (t * t * t * t * t * t)) / 40320.0;
        val northing = nfn + t1 + (dlam * dlam) * t2 + (dlam * dlam * dlam * dlam) * t3 + (dlam * dlam * dlam * dlam * dlam * dlam) * t4 + (dlam * dlam * dlam * dlam * dlam * dlam * dlam * dlam) * t5;
        val t6 = sn * c * ok;
        val t7 = sn * (c * c * c) * ok * (1.0 - (t * t) + eta) / 6.0;
        val t8 = sn * (c * c * c * c * c) * ok * (5.0 - 18.0 * (t * t) + (t * t * t * t) + 14.0 * eta - 58.0 * (t * t) * eta + 13.0 * (eta * eta) + 4.0 * (eta * eta * eta) - 64.0 * (t * t) * (eta * eta) - 24.0 * (t * t) * (eta * eta * eta)) / 120.0;
        val t9 = sn * (c * c * c * c * c * c * c) * ok * (61.0 - 479.0 * (t * t) + 179.0 *  (t * t * t * t) - (t * t * t * t * t * t)) / 5040.0;
        val easting = fe + strf + dlam * t6 + (dlam * dlam * dlam) * t7  + (dlam * dlam * dlam * dlam * dlam) * t8 + (dlam * dlam * dlam * dlam * dlam * dlam * dlam) * t9;// + 0.5;
        return Point(easting, northing)
    }

    private fun CalculateESquared(a: Double, b: Double): Double {
        return (a * a - b * b) / (a * a)
    }

    private fun CalculateE2Squared(a: Double, b: Double): Double {
        return (a * a - b * b) / (b * b)
    }

    private fun sphsn(a: Double, es: Double, sphi: Double): Double {
        val sinSphi = Math.sin(sphi)
        return a / Math.sqrt(1.0 - es * (sinSphi * sinSphi))
    }

    private fun sphtmd(ap: Double, bp: Double, cp: Double, dp: Double, ep: Double, sphi: Double): Double {
        return ap * sphi - bp * Math.sin(2.0 * sphi) + cp * Math.sin(4.0 * sphi) - dp * Math.sin(6.0 * sphi) + ep * Math.sin(8.0 * sphi)
    }
}