import 'package:flutter/material.dart';

import '../app_theme.dart';

/// Cuadro naranja con la "M" de MonyMonty.
class BrandMark extends StatelessWidget {
  const BrandMark({super.key, this.size = 44});

  final double size;

  @override
  Widget build(BuildContext context) {
    final colors = AppColors.of(context);

    return Container(
      width: size,
      height: size,
      alignment: Alignment.center,
      decoration: BoxDecoration(
        color: colors.brand,
        borderRadius: BorderRadius.circular(size * 0.3),
      ),
      child: Text(
        'M',
        style: TextStyle(
          color: Colors.white,
          fontSize: size * 0.55,
          fontWeight: FontWeight.w900,
          height: 1,
        ),
      ),
    );
  }
}

/// Texto "MonyMonty" con "Monty" en naranja.
class BrandWordmark extends StatelessWidget {
  const BrandWordmark({super.key, this.fontSize = 22});

  final double fontSize;

  @override
  Widget build(BuildContext context) {
    final colors = AppColors.of(context);

    return Text.rich(
      TextSpan(
        style: TextStyle(fontSize: fontSize, fontWeight: FontWeight.w800),
        children: [
          TextSpan(
            text: 'Mony',
            style: TextStyle(color: colors.title),
          ),
          TextSpan(
            text: 'Monty',
            style: TextStyle(color: colors.brand),
          ),
        ],
      ),
    );
  }
}

/// Logo completo: la marca y el texto, en fila o en columna.
class BrandLockup extends StatelessWidget {
  const BrandLockup({
    super.key,
    this.axis = Axis.horizontal,
    this.markSize = 36,
    this.fontSize = 20,
  });

  final Axis axis;
  final double markSize;
  final double fontSize;

  @override
  Widget build(BuildContext context) {
    final mark = BrandMark(size: markSize);
    final word = BrandWordmark(fontSize: fontSize);

    if (axis == Axis.horizontal) {
      return Row(
        mainAxisSize: MainAxisSize.min,
        children: [mark, const SizedBox(width: 10), word],
      );
    }

    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [mark, const SizedBox(height: 12), word],
    );
  }
}
