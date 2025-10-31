# sapcloudGAL
dockerExecute(
    script: this,
    dockerImage: 'python:3.10',
    dockerVolumeMounts: ['/home/user/.cache:/root/.cache'],  // Montar volúmenes
    dockerEnvVars: ['ENV=production'],                       // Variables de entorno
    dockerWorkdir: '/app',                                   // Directorio de trabajo
    reuseContainer: true                                     // Reusar contenedor (útil para depuración)
) {
    // tus pasos aquí
}

