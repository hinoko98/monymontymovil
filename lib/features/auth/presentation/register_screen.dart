import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../../core/app_theme.dart';
import '../../../core/widgets/app_fields.dart';
import '../state/auth_controller.dart';

/// Pantalla "Crea tu cuenta".
class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  static const _planes = ['Free', 'Basic', 'Premium'];
  static const _generos = ['Femenino', 'Masculino'];

  final _formKey = GlobalKey<FormState>();
  final _nombreController = TextEditingController();
  final _fechaController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmController = TextEditingController();

  DateTime? _fechaNacimiento;
  String? _plan;
  String? _genero;
  bool _acceptLegal = false;
  bool _legalError = false;
  bool _obscurePassword = true;
  bool _obscureConfirm = true;

  @override
  void dispose() {
    _nombreController.dispose();
    _fechaController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    _confirmController.dispose();
    super.dispose();
  }

  /// La API guarda nombre y apellido por separado, así que se parte el
  /// nombre completo: la primera palabra es el nombre y el resto el apellido.
  (String nombre, String apellido) _partirNombre(String value) {
    final partes = value.trim().split(RegExp(r'\s+'));
    if (partes.length < 2) return (partes.first, '');
    return (partes.first, partes.sublist(1).join(' '));
  }

  DateTime get _fechaMaxima {
    final hoy = DateTime.now();
    return DateTime(hoy.year - 18, hoy.month, hoy.day);
  }

  String? _validateNombre(String? value) {
    final text = value?.trim() ?? '';
    if (text.isEmpty) return 'El nombre es obligatorio';
    final (nombre, apellido) = _partirNombre(text);
    if (apellido.isEmpty) return 'Ingresa tu nombre y tu apellido';
    if (nombre.length < 3 || apellido.length < 3) {
      return 'El nombre y el apellido deben tener al menos 3 caracteres';
    }
    return null;
  }

  String? _validateEmail(String? value) {
    final email = value?.trim() ?? '';
    if (email.isEmpty) return 'El email es obligatorio';
    if (!RegExp(r'^[^@\s]+@[^@\s]+\.[^@\s]+$').hasMatch(email)) {
      return 'Formato de email inválido';
    }
    return null;
  }

  String? _validatePassword(String? value) {
    final v = value ?? '';
    if (v.length < 8) return 'Debe tener al menos 8 caracteres';
    if (!RegExp(r'[A-Z]').hasMatch(v)) {
      return 'Debe contener al menos una mayúscula';
    }
    if (!RegExp(r'\d').hasMatch(v)) return 'Debe contener al menos un número';
    if (!RegExp(r'[!@#$%^&*()_\-+=<>?{}\[\]~]').hasMatch(v)) {
      return 'Debe contener al menos un símbolo';
    }
    return null;
  }

  String? _validateConfirm(String? value) {
    if (value == null || value.isEmpty) return 'Confirma tu contraseña';
    if (value != _passwordController.text) {
      return 'Las contraseñas no coinciden';
    }
    return null;
  }

  String? _validateFecha(String? _) {
    if (_fechaNacimiento == null) return 'La fecha es obligatoria';
    if (_fechaNacimiento!.isAfter(_fechaMaxima)) {
      return 'Debes ser mayor de 18 años';
    }
    return null;
  }

  Future<void> _pickFecha() async {
    final maxima = _fechaMaxima;
    final picked = await showDatePicker(
      context: context,
      initialDate: _fechaNacimiento ?? maxima,
      firstDate: DateTime(1900),
      lastDate: maxima,
      helpText: 'Fecha de nacimiento',
    );
    if (picked == null) return;

    setState(() {
      _fechaNacimiento = picked;
      _fechaController.text =
          '${picked.day.toString().padLeft(2, '0')}/'
          '${picked.month.toString().padLeft(2, '0')}/${picked.year}';
    });
    _formKey.currentState?.validate();
  }

  Future<void> _onSubmit() async {
    final valid = _formKey.currentState?.validate() ?? false;
    setState(() => _legalError = !_acceptLegal);
    if (!valid || !_acceptLegal) return;
    FocusScope.of(context).unfocus();

    final (nombre, apellido) = _partirNombre(_nombreController.text);
    final auth = context.read<AuthController>();
    final success = await auth.register(
      nombre: nombre,
      apellido: apellido,
      fechaNacimiento: _fechaNacimiento!,
      genero: _genero!,
      email: _emailController.text.trim(),
      password: _passwordController.text,
      planId: _plan!.toLowerCase(),
    );

    if (!mounted) return;

    if (success) {
      Navigator.of(context).pop(true);
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(auth.errorMessage ?? 'Error en Crear Cuenta')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthController>();
    final colors = AppColors.of(context);
    final theme = Theme.of(context);
    final enabled = !auth.loading;

    return Scaffold(
      body: SafeArea(
        child: Center(
          child: SingleChildScrollView(
            keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
            padding: const EdgeInsets.fromLTRB(24, 12, 24, 24),
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 420),
              child: Form(
                key: _formKey,
                autovalidateMode: AutovalidateMode.onUserInteraction,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Align(
                      alignment: Alignment.centerLeft,
                      child: BackSquareButton(
                        onPressed: enabled
                            ? () => Navigator.of(context).maybePop()
                            : null,
                      ),
                    ),
                    const SizedBox(height: 24),
                    Text(
                      'Crea tu cuenta',
                      style: TextStyle(
                        fontSize: 26,
                        fontWeight: FontWeight.w800,
                        color: colors.title,
                      ),
                    ),
                    const SizedBox(height: 6),
                    Text(
                      'Tu camino hacia la libertad financiera comienza aquí.',
                      style: TextStyle(
                        fontSize: 15,
                        height: 1.4,
                        color: colors.muted,
                      ),
                    ),
                    const SizedBox(height: 24),
                    AppTextField(
                      label: 'Nombre completo',
                      hint: 'Nombre y apellido',
                      controller: _nombreController,
                      textCapitalization: TextCapitalization.words,
                      textInputAction: TextInputAction.next,
                      autofillHints: const [AutofillHints.name],
                      enabled: enabled,
                      validator: _validateNombre,
                    ),
                    const SizedBox(height: 16),
                    AppTextField(
                      label: 'Fecha de nacimiento',
                      hint: 'DD/MM/AAAA',
                      controller: _fechaController,
                      readOnly: true,
                      enabled: enabled,
                      onTap: _pickFecha,
                      validator: _validateFecha,
                      suffixIcon: const Icon(Icons.calendar_today_outlined),
                    ),
                    const SizedBox(height: 16),
                    AppDropdownField(
                      label: 'Plan',
                      hint: 'Selecciona un plan',
                      value: _plan,
                      items: _planes,
                      enabled: enabled,
                      errorText: 'Debes seleccionar un plan',
                      onChanged: (v) => setState(() => _plan = v),
                    ),
                    const SizedBox(height: 16),
                    AppDropdownField(
                      label: 'Género',
                      hint: 'Selecciona un género',
                      value: _genero,
                      items: _generos,
                      enabled: enabled,
                      errorText: 'Debes seleccionar un género',
                      onChanged: (v) => setState(() => _genero = v),
                    ),
                    const SizedBox(height: 16),
                    AppTextField(
                      label: 'Correo electrónico',
                      hint: 'tucorreo@ejemplo.com',
                      controller: _emailController,
                      keyboardType: TextInputType.emailAddress,
                      textInputAction: TextInputAction.next,
                      autofillHints: const [AutofillHints.email],
                      enabled: enabled,
                      validator: _validateEmail,
                    ),
                    const SizedBox(height: 16),
                    AppTextField(
                      label: 'Contraseña',
                      hint: 'Mínimo 8 caracteres',
                      controller: _passwordController,
                      obscureText: _obscurePassword,
                      textInputAction: TextInputAction.next,
                      autofillHints: const [AutofillHints.newPassword],
                      enabled: enabled,
                      validator: _validatePassword,
                      suffixIcon: IconButton(
                        icon: Icon(
                          _obscurePassword
                              ? Icons.visibility_outlined
                              : Icons.visibility_off_outlined,
                        ),
                        onPressed: () => setState(
                          () => _obscurePassword = !_obscurePassword,
                        ),
                      ),
                    ),
                    const SizedBox(height: 16),
                    AppTextField(
                      label: 'Confirmar contraseña',
                      hint: 'Repite tu contraseña',
                      controller: _confirmController,
                      obscureText: _obscureConfirm,
                      textInputAction: TextInputAction.done,
                      enabled: enabled,
                      validator: _validateConfirm,
                      onFieldSubmitted: (_) => _onSubmit(),
                      suffixIcon: IconButton(
                        icon: Icon(
                          _obscureConfirm
                              ? Icons.visibility_outlined
                              : Icons.visibility_off_outlined,
                        ),
                        onPressed: () =>
                            setState(() => _obscureConfirm = !_obscureConfirm),
                      ),
                    ),
                    const SizedBox(height: 16),
                    InkWell(
                      borderRadius: BorderRadius.circular(8),
                      onTap: enabled
                          ? () => setState(() {
                              _acceptLegal = !_acceptLegal;
                              _legalError = false;
                            })
                          : null,
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Checkbox(
                            value: _acceptLegal,
                            onChanged: enabled
                                ? (v) => setState(() {
                                    _acceptLegal = v ?? false;
                                    _legalError = false;
                                  })
                                : null,
                          ),
                          Expanded(
                            child: Padding(
                              padding: const EdgeInsets.only(top: 11),
                              child: Text.rich(
                                TextSpan(
                                  style: TextStyle(
                                    fontSize: 13.5,
                                    height: 1.45,
                                    color: colors.muted,
                                  ),
                                  children: [
                                    const TextSpan(text: 'Acepto los '),
                                    TextSpan(
                                      text: 'Términos y condiciones',
                                      style: TextStyle(
                                        color: colors.primary,
                                        fontWeight: FontWeight.w700,
                                      ),
                                    ),
                                    const TextSpan(text: ' y la '),
                                    TextSpan(
                                      text: 'Política de privacidad',
                                      style: TextStyle(
                                        color: colors.primary,
                                        fontWeight: FontWeight.w700,
                                      ),
                                    ),
                                    const TextSpan(text: '.'),
                                  ],
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    if (_legalError)
                      Padding(
                        padding: const EdgeInsets.only(left: 12, top: 4),
                        child: Text(
                          'Debes aceptar los términos y la política de privacidad para continuar',
                          style: TextStyle(
                            fontSize: 12,
                            color: theme.colorScheme.error,
                          ),
                        ),
                      ),
                    const SizedBox(height: 24),
                    FilledButton(
                      onPressed: enabled ? _onSubmit : null,
                      child: auth.loading
                          ? const SizedBox(
                              height: 22,
                              width: 22,
                              child: CircularProgressIndicator(
                                strokeWidth: 2.4,
                                color: Colors.white,
                              ),
                            )
                          : const Text('Crear Cuenta'),
                    ),
                    const SizedBox(height: 16),
                    Center(
                      child: GestureDetector(
                        onTap: enabled
                            ? () => Navigator.of(context).maybePop()
                            : null,
                        child: Text.rich(
                          TextSpan(
                            style: TextStyle(fontSize: 14, color: colors.muted),
                            children: [
                              const TextSpan(text: '¿Ya tienes cuenta? '),
                              TextSpan(
                                text: 'Inicia sesión',
                                style: TextStyle(
                                  color: colors.primary,
                                  fontWeight: FontWeight.w700,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
