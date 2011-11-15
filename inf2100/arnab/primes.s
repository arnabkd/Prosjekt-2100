        .data
        .globl  prime
prime:  .fill   4004                    # int prime[1001];
        .globl  LF
LF:     .fill   4                       # int LF;
        .text
        .globl  find_primes             
find_primes:
        pushl   %ebp                    # Start function find_primes
        movl    %esp,%ebp               
        subl    $8,%esp                 # Get 8 bytes local data space
        movl    $2,%eax                 # 2
        movl    %eax,-4(%ebp)           # i1 =
.L0001:                                 # Start for-statement
        movl    -4(%ebp),%eax           # i1
        pushl   %eax                    
        movl    $1000,%eax              # 1000
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setle   %al                     # Test <=
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0002                  
        movl    $2,%eax                 # 2
        pushl   %eax                    
        movl    -4(%ebp),%eax           # i1
        movl    %eax,%ecx               
        popl    %eax                    
        imull   %ecx,%eax               # Compute *
        movl    %eax,-8(%ebp)           # i2 =
.L0003:                                 # Start for-statement
        movl    -8(%ebp),%eax           # i2
        pushl   %eax                    
        movl    $1000,%eax              # 1000
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setle   %al                     # Test <=
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0004                  
        movl    -8(%ebp),%eax           # i2
        pushl   %eax                    
        movl    $0,%eax                 # 0
        leal    prime,%edx              # prime[...] = 
        popl    %ecx                    
        movl    %eax,(%edx,%ecx,4)      
        movl    -8(%ebp),%eax           # i2
        pushl   %eax                    
        movl    -4(%ebp),%eax           # i1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-8(%ebp)           # i2 =
        jmp     .L0003                  
.L0004:                                 # End for-statement
        movl    -4(%ebp),%eax           # i1
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-4(%ebp)           # i1 =
        jmp     .L0001                  
.L0002:                                 # End for-statement
.exit$find_primes:
        addl    $8,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function find_primes
        .globl  mod                     
mod:    pushl   %ebp                    # Start function mod
        movl    %esp,%ebp               
        movl    8(%ebp),%eax            # a
        pushl   %eax                    
        movl    8(%ebp),%eax            # a
        pushl   %eax                    
        movl    12(%ebp),%eax           # b
        movl    %eax,%ecx               
        popl    %eax                    
        cdq                             
        idivl   %ecx                    # Compute /
        pushl   %eax                    
        movl    12(%ebp),%eax           # b
        movl    %eax,%ecx               
        popl    %eax                    
        imull   %ecx,%eax               # Compute *
        movl    %eax,%ecx               
        popl    %eax                    
        subl    %ecx,%eax               # Compute -
        jmp     .exit$mod               # return-statement
.exit$mod:                                
        popl    %ebp                    
        ret                             # End function mod
        .globl  n_chars                 
n_chars:
        pushl   %ebp                    # Start function n_chars
        movl    %esp,%ebp               
                                        # Start if-statement
        movl    8(%ebp),%eax            # a
        pushl   %eax                    
        movl    $0,%eax                 # 0
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setl    %al                     # Test <
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0005                  
        movl    $1,%eax                 # 1
        pushl   %eax                    
        movl    $0,%eax                 # 0
        pushl   %eax                    
        movl    8(%ebp),%eax            # a
        movl    %eax,%ecx               
        popl    %eax                    
        subl    %ecx,%eax               # Compute -
        pushl   %eax                    # Push parameter #1
        call    n_chars                 # Call n_chars
        popl    %ecx                    # Pop parameter #1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        jmp     .exit$n_chars           # return-statement
.L0005:                                 # End if-statement
                                        # Start if-statement
        movl    8(%ebp),%eax            # a
        pushl   %eax                    
        movl    $9,%eax                 # 9
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setle   %al                     # Test <=
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0006                  
        movl    $1,%eax                 # 1
        jmp     .exit$n_chars           # return-statement
.L0006:                                 # End if-statement
        movl    8(%ebp),%eax            # a
        pushl   %eax                    
        movl    $10,%eax                # 10
        movl    %eax,%ecx               
        popl    %eax                    
        cdq                             
        idivl   %ecx                    # Compute /
        pushl   %eax                    # Push parameter #1
        call    n_chars                 # Call n_chars
        popl    %ecx                    # Pop parameter #1
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        jmp     .exit$n_chars           # return-statement
.exit$n_chars:                                
        popl    %ebp                    
        ret                             # End function n_chars
        .globl  pn                      
pn:     pushl   %ebp                    # Start function pn
        movl    %esp,%ebp               
        subl    $4,%esp                 # Get 4 bytes local data space
        movl    8(%ebp),%eax            # v
        pushl   %eax                    # Push parameter #1
        call    n_chars                 # Call n_chars
        popl    %ecx                    # Pop parameter #1
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-4(%ebp)           # i =
.L0007:                                 # Start for-statement
        movl    -4(%ebp),%eax           # i
        pushl   %eax                    
        movl    12(%ebp),%eax           # w
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setle   %al                     # Test <=
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0008                  
        movl    $32,%eax                # 32
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        popl    %ecx                    # Pop parameter #1
        movl    -4(%ebp),%eax           # i
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-4(%ebp)           # i =
        jmp     .L0007                  
.L0008:                                 # End for-statement
        movl    8(%ebp),%eax            # v
        pushl   %eax                    # Push parameter #1
        call    putint                  # Call putint
        popl    %ecx                    # Pop parameter #1
.exit$pn:
        addl    $4,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function pn
        .globl  and                     
and:    pushl   %ebp                    # Start function and
        movl    %esp,%ebp               
                                        # Start if-statement
        movl    8(%ebp),%eax            # a
        cmpl    $0,%eax                 
        je      .L0010                  
        movl    12(%ebp),%eax           # b
        jmp     .exit$and               # return-statement
        jmp     .L0009                  
.L0010:                                 #   else-part
        movl    $0,%eax                 # 0
        jmp     .exit$and               # return-statement
.L0009:                                 # End if-statement
.exit$and:                                
        popl    %ebp                    
        ret                             # End function and
        .globl  print_primes            
print_primes:
        pushl   %ebp                    # Start function print_primes
        movl    %esp,%ebp               
        subl    $8,%esp                 # Get 8 bytes local data space
        movl    $0,%eax                 # 0
        movl    %eax,-4(%ebp)           # n_printed =
        movl    $1,%eax                 # 1
        movl    %eax,-8(%ebp)           # i =
.L0011:                                 # Start for-statement
        movl    -8(%ebp),%eax           # i
        pushl   %eax                    
        movl    $1000,%eax              # 1000
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setle   %al                     # Test <=
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0012                  
                                        # Start if-statement
        movl    -8(%ebp),%eax           # i
        leal    prime,%edx              # prime[...]
        movl    (%edx,%eax,4),%eax      
        cmpl    $0,%eax                 
        je      .L0013                  
                                        # Start if-statement
        movl    -4(%ebp),%eax           # n_printed
        pushl   %eax                    
        movl    $0,%eax                 # 0
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setg    %al                     # Test >
        movzbl  %al,%eax                
        pushl   %eax                    # Push parameter #2
        movl    $10,%eax                # 10
        pushl   %eax                    # Push parameter #2
        movl    -4(%ebp),%eax           # n_printed
        pushl   %eax                    # Push parameter #1
        call    mod                     # Call mod
        popl    %ecx                    # Pop parameter #1
        popl    %ecx                    # Pop parameter #2
        pushl   %eax                    
        movl    $0,%eax                 # 0
        popl    %ecx                    
        cmpl    %eax,%ecx               
        sete    %al                     # Test ==
        movzbl  %al,%eax                
        pushl   %eax                    # Push parameter #1
        call    and                     # Call and
        popl    %ecx                    # Pop parameter #1
        popl    %ecx                    # Pop parameter #2
        cmpl    $0,%eax                 
        je      .L0014                  
        movl    LF,%eax                 # LF
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        popl    %ecx                    # Pop parameter #1
.L0014:                                 # End if-statement
        movl    $32,%eax                # 32
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        popl    %ecx                    # Pop parameter #1
        movl    $3,%eax                 # 3
        pushl   %eax                    # Push parameter #2
        movl    -8(%ebp),%eax           # i
        pushl   %eax                    # Push parameter #1
        call    pn                      # Call pn
        popl    %ecx                    # Pop parameter #1
        popl    %ecx                    # Pop parameter #2
        movl    -4(%ebp),%eax           # n_printed
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-4(%ebp)           # n_printed =
.L0013:                                 # End if-statement
        movl    -8(%ebp),%eax           # i
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-8(%ebp)           # i =
        jmp     .L0011                  
.L0012:                                 # End for-statement
        movl    LF,%eax                 # LF
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        popl    %ecx                    # Pop parameter #1
.exit$print_primes:
        addl    $8,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function print_primes
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $4,%esp                 # Get 4 bytes local data space
        movl    $10,%eax                # 10
        movl    %eax,LF                 # LF =
        movl    $1,%eax                 # 1
        pushl   %eax                    
        movl    $0,%eax                 # 0
        leal    prime,%edx              # prime[...] = 
        popl    %ecx                    
        movl    %eax,(%edx,%ecx,4)      
        movl    $2,%eax                 # 2
        movl    %eax,-4(%ebp)           # i =
.L0015:                                 # Start for-statement
        movl    -4(%ebp),%eax           # i
        pushl   %eax                    
        movl    $1000,%eax              # 1000
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setle   %al                     # Test <=
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0016                  
        movl    -4(%ebp),%eax           # i
        pushl   %eax                    
        movl    $1,%eax                 # 1
        leal    prime,%edx              # prime[...] = 
        popl    %ecx                    
        movl    %eax,(%edx,%ecx,4)      
        movl    -4(%ebp),%eax           # i
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-4(%ebp)           # i =
        jmp     .L0015                  
.L0016:                                 # End for-statement
        call    find_primes             # Call find_primes
        call    print_primes            # Call print_primes
        movl    $0,%eax                 # 0
        jmp     .exit$main              # return-statement
.exit$main:
        addl    $4,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function main
