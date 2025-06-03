export const hashPassword = async (
  password: string,
  salt?: Uint8Array,
): Promise<{ hash: string; salt: string }> => {
  // 生成随机盐值（128位）
  const saltBuffer = salt || crypto.getRandomValues(new Uint8Array(16))
  const encoder = new TextEncoder()

  // 导入密码
  const keyMaterial = await crypto.subtle.importKey(
    'raw',
    encoder.encode(password),
    'PBKDF2',
    false,
    ['deriveBits'],
  )

  // 执行PBKDF2哈希
  const derivedBits = await crypto.subtle.deriveBits(
    {
      name: 'PBKDF2',
      salt: saltBuffer,
      iterations: 100000,
      hash: 'SHA-256',
    },
    keyMaterial,
    256, // 输出256位(32字节)
  )

  // 转换为十六进制字符串
  const hashArray = Array.from(new Uint8Array(derivedBits))
  const hashHex = hashArray.map((b) => b.toString(16).padStart(2, '0')).join('')

  return {
    hash: hashHex,
    salt: Array.from(saltBuffer)
      .map((b) => b.toString(16).padStart(2, '0'))
      .join(''),
  }
}

export const verifyPassword = async (
  password: string,
  hash: string,
  salt: string,
): Promise<boolean> => {
  const saltBuffer = Uint8Array.from(
    salt.match(/.{1,2}/g)!.map((byte) => parseInt(byte, 16)),
  )

  const { hash: newHash } = await hashPassword(password, saltBuffer)
  return newHash === hash
}
